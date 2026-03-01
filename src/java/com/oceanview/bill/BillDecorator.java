package com.oceanview.bill;

import com.itextpdf.text.Document;

public abstract class BillDecorator implements Bill {

    protected Bill bill;

    public BillDecorator(Bill bill) {
        this.bill = bill;
    }

    @Override
    public void generate(Document document) throws Exception {
        bill.generate(document);
    }
}