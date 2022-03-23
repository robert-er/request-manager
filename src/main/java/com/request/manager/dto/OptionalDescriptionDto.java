package com.request.manager.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class OptionalDescriptionDto {

    private Optional<String> description;
}
