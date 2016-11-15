package com.ocado.pandateam.newrelic.sync;

import lombok.Value;

@Value
public class EmailChannel {

    private String channelName;

    private String emailAddress;

    private Boolean includeJsonAttachment;

    public String getIncludeJsonAttachment(){
        return includeJsonAttachment == null ? null : String.valueOf(includeJsonAttachment);
    }
}
