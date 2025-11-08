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
 * AssignRoleRequestDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-07T20:44:56.726508700-03:00[America/Asuncion]")
public class AssignRoleRequestDTO {

  private Long roleId;

  public AssignRoleRequestDTO() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AssignRoleRequestDTO(Long roleId) {
    this.roleId = roleId;
  }

  public AssignRoleRequestDTO roleId(Long roleId) {
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
    AssignRoleRequestDTO assignRoleRequestDTO = (AssignRoleRequestDTO) o;
    return Objects.equals(this.roleId, assignRoleRequestDTO.roleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssignRoleRequestDTO {\n");
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

