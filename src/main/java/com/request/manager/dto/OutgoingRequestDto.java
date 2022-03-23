package com.request.manager.dto;

import com.request.manager.domain.Status;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
@Builder
public class OutgoingRequestDto {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;
    private UUID uuid;

    @NotNull
    private Status status;
    private String details;
}
