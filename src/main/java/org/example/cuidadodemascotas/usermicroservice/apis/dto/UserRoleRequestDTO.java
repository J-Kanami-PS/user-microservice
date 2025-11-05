package org.example.cuidadodemascotas.usermicroservice.apis.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserRoleRequestDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-05T00:02:41.133339100-03:00[America/Asuncion]")
public class UserRoleRequestDTO {

  private Long userId;

  private Long roleId;

  public UserRoleRequestDTO() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserRoleRequestDTO(Long userId, Long roleId) {
    this.userId = userId;
    this.roleId = roleId;
  }

  public UserRoleRequestDTO userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * ID del usuario
   * @return userId
  */
  @NotNull 
  @Schema(name = "userId", description = "ID del usuario", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userId")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public UserRoleRequestDTO roleId(Long roleId) {
    this.roleId = roleId;
    return this;
  }

  /**
   * ID del rol a asignar
   * @return roleId
  */
  @NotNull 
  @Schema(name = "roleId", description = "ID del rol a asignar", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roleId")
  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRoleRequestDTO userRoleRequestDTO = (UserRoleRequestDTO) o;
    return Objects.equals(this.userId, userRoleRequestDTO.userId) &&
        Objects.equals(this.roleId, userRoleRequestDTO.roleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, roleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRoleRequestDTO {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

