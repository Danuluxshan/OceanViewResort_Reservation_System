package com.oceanview.bill;

import com.itextpdf.text.Document;

public interface Bill {
    void generate(Document document) throws Exception;
}