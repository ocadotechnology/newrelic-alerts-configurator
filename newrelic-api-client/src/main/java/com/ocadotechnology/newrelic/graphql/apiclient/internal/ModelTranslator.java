package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannel;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannelConfiguration;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannelLinks;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Expiration;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Signal;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Nrql;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Consumer;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.ocadotechnology.newrelic.graphql.apiclient.internal.util.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class ModelTranslator {
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper()
            .disable(FAIL_ON_UNKNOWN_PROPERTIES);

    private final ObjectMapper objectMapper;

    public ModelTranslator() {
        this.objectMapper = DEFAULT_OBJECT_MAPPER;
    }

    public AlertsChannel toModel(NotificationChannel notificationChannel) {
        return Optional.ofNullable(notificationChannel)
                .map(channel -> AlertsChannel.builder()
                        .id(Optional.ofNullable(channel.getId()).map(Integer::parseInt).orElse(null))
                        .name(channel.getName())
                        .type(Optional.ofNullable(channel.getType()).map(String::toLowerCase).orElse(null))
                        .links(new AlertsChannelLinks(Optional.ofNullable(channel.getAssociatedPolicies())
                                .map(AssociatedPolicies::getPolicies)
                                .orElse(Collections.emptyList())
                                .stream().map(Identifiable::getId)
                                .collect(toList())))
                        .configuration(toModel(channel.getConfig()))
                        .build())
                .orElse(null);
    }

    public AlertsNrqlCondition toModel(NrqlCondition dto) {
        return AlertsNrqlCondition.builder()
                .id(Optional.ofNullable(dto.getId()).map(Integer::parseInt).orElse(null))
                .name(dto.getName())
                .enabled(dto.getEnabled())
                .runbookUrl(dto.getRunbookUrl())
                .nrql(Optional.ofNullable(dto.getNrql())
                        .map(nrql -> new Nrql(nrql.getQuery()))
                        .orElse(null))
                .expiration(Optional.ofNullable(dto.getExpiration())
                        .map(expiration -> Expiration.builder()
                                .closeViolationsOnExpiration(expiration.isCloseViolationsOnExpiration())
                                .expirationDuration(expiration.getExpirationDuration())
                                .openViolationOnExpiration(expiration.isOpenViolationOnExpiration())
                                .build())
                        .orElse(null))
                .signal(Optional.ofNullable(dto.getSignal())
                        .map(signal -> Signal.builder()
                                .aggregationDelay(signal.getAggregationDelay())
                                .aggregationMethod(Optional.ofNullable(signal.getAggregationMethod()).map(String::toUpperCase).orElse(null))
                                .aggregationTimer(signal.getAggregationTimer())
                                .aggregationWindow(signal.getAggregationWindow())
                                .fillOption(Optional.ofNullable(signal.getFillOption()).map(String::toLowerCase).orElse(null))
                                .fillValue(signal.getFillValue())
                                .build())
                        .orElse(null))
                .terms(Optional.ofNullable(dto.getTerms())
                        .map(terms -> terms.stream()
                                .map(term -> Terms.builder()
                                        .operator(Optional.ofNullable(term.getOperator()).map(String::toLowerCase).orElse(null))
                                        .priority(Optional.ofNullable(term.getPriority()).map(String::toLowerCase).orElse(null))
                                        .threshold(term.getThreshold())
                                        .duration(Optional.ofNullable(term.getThresholdDuration())
                                                .map(Long::parseLong)
                                                .map(durationSeconds -> "" + (long) (durationSeconds / 60))
                                                .orElse(null))
                                        .timeFunction(Optional.ofNullable(term.getThresholdOccurrences())
                                                .map(this::thresholdOccurrencesToTimeFunctionTo)
                                                .map(String::toLowerCase)
                                                .orElse(null))
                                        .build())
                                .collect(toList()))
                        .orElse(null))
                .valueFunction(Optional.ofNullable(dto.getValueFunction()).map(String::toLowerCase).orElse(null))
                .build();
    }

    private AlertsChannelConfiguration toModel(NotificationChannelConfig dto) {
        return Optional.ofNullable(dto)
                .filter(config -> !config.isEmpty())
                .map(config -> AlertsChannelConfiguration.builder()
                        .recipients(Optional.ofNullable(config.getEmails()).orElse(emptyList())
                                .stream().reduce(null, (acc, email) -> (isNull(acc) ? "" : acc + ",") + email))
                        .includeJsonAttachment(config.getIncludeJson())
                        .userId(Optional.ofNullable(config.getUserId())
                                .map(Integer::parseInt).orElse(null))
                        .channel(config.getTeamChannel())
                        .baseUrl(config.getBaseUrl())
                        .headers(Optional.ofNullable(config.getCustomHttpHeaders())
                                .map(httpHeaders -> httpHeaders.stream().collect(toMap(HttpHeader::getName, HttpHeader::getValue)))
                                .filter(httpHeaders -> !httpHeaders.isEmpty())
                                .orElse(null))
                        .authUsername(Optional.ofNullable(config.getBasicAuth()).map(NotificationChannelConfig.BasicAuth::getUsername).orElse(null))
                        .payloadType(payloadTypeToModel(config.getCustomPayloadType()))
                        .payload(payloadToModel(config.getCustomPayloadType(), config.getCustomPayloadBody()))
                        .build()
                ).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> toChannelConfig(String type, String name, AlertsChannelConfiguration configuration) {
        checkArgument(nonNull(type), "channel type cannot be null");
        checkArgument(nonNull(name), "channel name cannot be null");
        checkArgument(nonNull(configuration), "configuration name cannot be null");

        Map<String, Object> config = map();
        config.put("name", name);
        String wrapperName = type;
        switch (type) {
            case "email":
                config.put("emails", Optional.ofNullable(configuration.getRecipients())
                        .map(recipients -> Arrays.stream(recipients.split(",")).map(String::trim).collect(toList())).orElse(null));
                config.put("includeJson", Optional.ofNullable(configuration.getIncludeJsonAttachment()).orElse(false));
                break;
            case "pagerduty":
                config.put("apiKey", configuration.getServiceKey());
                wrapperName = "pagerDuty";
                break;
            case "slack":
                config.put("teamChannel", configuration.getChannel());
                config.put("url", configuration.getUrl());
                break;
            case "webhook":
                AlertsWebhookCustomPayloadType payloadType = Optional.ofNullable(configuration.getPayloadType())
                        .map(AlertsWebhookCustomPayloadType::byType).orElse(null);

                config.put("baseUrl", configuration.getBaseUrl());
                config.put("basicAuth", Optional.ofNullable(configuration.getAuthUsername())
                        .map(__ -> {
                            Map<String, Object> basicAuth = map();
                            basicAuth.put("username", configuration.getAuthUsername());
                            basicAuth.put("password", configuration.getAuthPassword());
                            return basicAuth;
                        }).orElse(null));
                config.put("customPayloadType", Optional.ofNullable(payloadType)
                        .map(t -> new JavaLikeEnum(t.toString())).orElse(null));
                config.put("customHttpHeaders", Optional.ofNullable(configuration.getHeaders())
                        .map(headers -> headers.entrySet().stream().map(header -> {
                            Map<String, String> headerObject = map();
                            headerObject.put("name", header.getKey());
                            headerObject.put("value", header.getValue());
                            return headerObject;
                        }).collect(toList())).orElse(null));
                config.put("customPayloadBody", Optional.ofNullable(configuration.getPayload())
                        .flatMap(__ -> Optional.ofNullable(payloadType))
                        .map(pt -> {
                            switch (pt) {
                                case JSON:
                                    return writeJson(configuration.getPayload());
                                case FORM:
                                    return configuration.getPayload() instanceof String
                                            ? configuration.getPayload()
                                            : writeProperties((Map<String, Object>) configuration.getPayload());
                            }
                            return null;
                        }).orElse(null)
                );
                break;
            default:
                throw new IllegalArgumentException(format("Unsupported channel type: '%s'", type));
        }

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put(wrapperName, config);
        return wrapper;
    }

    public Map<String, Object> toNrqlCondition(AlertsNrqlCondition condition) {
        Map<String, Object> cond = map();
        cond.put("enabled", condition.getEnabled());
        cond.put("name", condition.getName());
        onPresent(condition.getRunbookUrl(), runbookUrl -> cond.put("runbookUrl", runbookUrl));
        Map<String, Object> nrql = map();
        nrql.put("query", Optional.ofNullable(condition.getNrql()).map(Nrql::getQuery).orElse(null));
        cond.put("nrql", nrql);
        onPresent(condition.getExpiration(), expiration -> {
            Map<String, Object> result = map();
            result.put("closeViolationsOnExpiration", expiration.isCloseViolationsOnExpiration());
            result.put("openViolationOnExpiration", expiration.isOpenViolationOnExpiration());
            onPresent(expiration.getExpirationDuration(),
                    expirationDuration -> result.put("expirationDuration", toLong(expiration.getExpirationDuration())));
            cond.put("expiration", result);
        });
        onPresent(condition.getSignal(), signal -> {
            Map<String, Object> result = map();
            onPresent(signal.getAggregationDelay(), aggregationDelay -> result.put("aggregationDelay", toLong(aggregationDelay)));
            onPresent(signal.getAggregationMethod(), aggregationMethod -> result.put("aggregationMethod", toEnum(aggregationMethod)));
            onPresent(signal.getAggregationTimer(), aggregationTimer -> result.put("aggregationTimer", toLong(aggregationTimer)));
            onPresent(signal.getAggregationWindow(), aggregationWindow -> result.put("aggregationWindow", toLong(aggregationWindow)));
            onPresent(signal.getFillOption(), fillOption -> result.put("fillOption", toEnum(fillOption)));
            onPresent(signal.getFillValue(), fillValue -> result.put("fillValue", fillValue));
            cond.put("signal", result);
        });
        cond.put("terms", Optional.ofNullable(condition.getTerms()).orElseGet(ArrayList::new).stream()
                .map(term -> {
                    LinkedHashMap<String, Object> result = map();
                    onPresent(term.getOperator(), operator -> result.put("operator", toEnum(operator)));
                    onPresent(term.getPriority(), priority -> result.put("priority", toEnum(priority)));
                    onPresent(term.getThreshold(), threshold -> result.put("threshold", toFloat(threshold)));
                    onPresent(term.getDuration(), duration -> result.put("thresholdDuration", toLong(duration) * 60));
                    onPresent(term.getTimeFunction(), timeFunction -> result.put("thresholdOccurrences", toEnum(timeFunctionToThresholdOccurrences(timeFunction))));
                    return result;
                })
                .collect(toList()));
        onPresent(condition.getValueFunction(), valueFunction -> cond.put("valueFunction", toEnum(valueFunction)));
        return cond;
    }

    private String timeFunctionToThresholdOccurrences(String input) {
        return input.equalsIgnoreCase("any") ? "at_least_once" : input;
    }

    private String thresholdOccurrencesToTimeFunctionTo(String input) {
        return input.equalsIgnoreCase("at_least_once") ? "any" : input;
    }

    private static <T1, T2> LinkedHashMap<T1, T2> map() {
        return new LinkedHashMap<>();
    }

    private JavaLikeEnum toEnum(String fillOption) {
        return new JavaLikeEnum(fillOption.toUpperCase());
    }

    private long toLong(String value) {
        return Long.parseLong(value);
    }

    private float toFloat(String value) {
        return Float.parseFloat(value);
    }

    private <T> void onPresent(T object, Consumer<T> consumer) {
        if (Objects.nonNull(object)) {
            consumer.accept(object);
        }
    }

    private String payloadTypeToModel(String customPayloadType) {
        return Optional.ofNullable(customPayloadType)
                .map(AlertsWebhookCustomPayloadType::valueOf)
                .map(AlertsWebhookCustomPayloadType::getType)
                .orElse(null);
    }

    private Map<String, Object> payloadToModel(String customPayloadType, String customPayloadBody) {
        return Optional.ofNullable(customPayloadBody)
                .map(__ -> customPayloadType)
                .map(AlertsWebhookCustomPayloadType::valueOf)
                .map(type -> {
                    switch (type) {
                        case JSON:
                            return readJson(StringEscapeUtils.unescapeJson(customPayloadBody));
                        case FORM:
                            return readProperties(customPayloadBody);
                        default:
                            return null;
                    }
                })
                .orElse(null);
    }

    @SneakyThrows
    private String writeJson(Object value) {
        return objectMapper.writeValueAsString(value);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private Map<String, Object> readJson(String value) {
        return objectMapper.readValue(value, Map.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SneakyThrows
    private Map<String, Object> readProperties(String value) {
        Properties properties = new Properties();
        properties.load(new StringReader(value));
        return (Map<String, Object>) (Map) properties;
    }

    @SneakyThrows
    public static String writeProperties(Map<String, Object> value) {
        Properties properties = new Properties();
        properties.putAll(value);
        StringWriter writer = new StringWriter();
        properties.store(writer, null);
        String output = writer.toString();
        String sep = System.getProperty("line.separator");
        return output.substring(output.indexOf(sep) + sep.length()).trim().replaceAll(sep, "\n");
    }
}
