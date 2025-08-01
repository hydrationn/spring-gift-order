package gift.order.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OutOfStockException extends CustomException {
    public OutOfStockException(int currentQuantity) {
        super("수량이 부족합니다. 현재 재고: " + currentQuantity, HttpStatus.BAD_REQUEST);
    }
}