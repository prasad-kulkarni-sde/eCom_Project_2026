package org.wolfsRealm.ecom_project_2026.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    private Long userId;

    private String userName;

    private String email;

    private List<String> roles= new ArrayList<>();


}
