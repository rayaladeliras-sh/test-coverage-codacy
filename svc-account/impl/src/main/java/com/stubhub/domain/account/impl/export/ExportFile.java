package com.stubhub.domain.account.impl.export;

import java.util.ArrayList;
import java.util.List;

public class ExportFile {

    private List<String> headers = new ArrayList<String>();
    private List<String[]> data = new ArrayList<String[]>();

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

}

