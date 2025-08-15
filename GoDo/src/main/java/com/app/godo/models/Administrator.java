package com.app.godo.models;

import jakarta.persistence.DiscriminatorValue;
import lombok.*;

@Getter
@Setter
@DiscriminatorValue("ADMIN")
public class Administrator extends User {}
