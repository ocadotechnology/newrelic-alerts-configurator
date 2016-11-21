package com.ocado.pandateam.newrelic.api.internal.pagination;

import com.mashape.unirest.http.HttpResponse;
import lombok.Getter;

@Getter
class PageableResponse {

    private static final String LINK_HEADER = "Link";

    private static final String META_REL = "rel";
    private static final String META_LAST = "last";
    private static final String META_NEXT = "next";
    private static final String META_FIRST = "first";
    private static final String META_PREV = "prev";

    private static final String LINK_DELIMITER = ",";
    private static final String LINK_PARAM_DELIMITER = ";";

    private String first;
    private String last;
    private String next;
    private String prev;

    PageableResponse(HttpResponse response) {
        String[] links = response.getHeaders().getFirst(LINK_HEADER).split(LINK_DELIMITER);
        for (String link : links) {
            String[] segments = link.split(LINK_PARAM_DELIMITER);
            if (segments.length < 2)
                continue;

            String linkPart = segments[0].trim();
            if (!linkPart.startsWith("<") || !linkPart.endsWith(">"))
                continue;
            linkPart = linkPart.substring(1, linkPart.length() - 1);

            for (int i = 1; i < segments.length; i++) {
                String[] rel = segments[i].trim().split("=");
                if (rel.length < 2 || !META_REL.equals(rel[0]))
                    continue;

                String relValue = rel[1];
                if (relValue.startsWith("\"") && relValue.endsWith("\""))
                    relValue = relValue.substring(1, relValue.length() - 1);

                if (META_FIRST.equals(relValue))
                    first = linkPart;
                else if (META_LAST.equals(relValue))
                    last = linkPart;
                else if (META_NEXT.equals(relValue))
                    next = linkPart;
                else if (META_PREV.equals(relValue))
                    prev = linkPart;
            }
        }
    }
}
