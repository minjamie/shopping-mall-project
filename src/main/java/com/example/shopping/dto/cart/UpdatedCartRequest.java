package com.example.shopping.dto.cart;

import lombok.Data;
import lombok.Getter;

@Getter
public class UpdatedCartRequest extends AddCartRequest{
    private Integer productId;
    private boolean isDelete;

    public boolean getIsDelete() {
        return isDelete;
    }
}
