package com.el_buen_corte.el_buen_corte.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String observations;
}
