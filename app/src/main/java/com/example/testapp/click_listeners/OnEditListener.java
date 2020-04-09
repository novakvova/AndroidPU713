package com.example.testapp.click_listeners;

import com.example.testapp.network.ProductEntry;
import com.example.testapp.productview.dto.ProductDTO;

public interface OnEditListener {
    void editItem(ProductEntry productEntry, int index);
}
