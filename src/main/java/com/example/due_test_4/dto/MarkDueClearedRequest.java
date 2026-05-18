package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Builder()
@Data()
public class MarkDueClearedRequest {

    private LocalDate clearedDate;

    public LocalDate getClearedDate() {
        return this.clearedDate;
    }

    public void setClearedDate(LocalDate clearedDate) {
        this.clearedDate = clearedDate;
    }

    public MarkDueClearedRequest() {
    }

    public MarkDueClearedRequest(LocalDate clearedDate) {
        this.clearedDate = clearedDate;
    }
}
