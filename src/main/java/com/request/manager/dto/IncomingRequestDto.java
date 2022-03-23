package com.request.manager.dto;

import com.sun.istack.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class IncomingRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String description;
}
