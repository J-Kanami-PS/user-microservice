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
 * UserRoleCheckResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T03:29:16.086620800-03:00[America/Asuncion]")
public class UserRoleCheckResponse {

  private Long userId;

  private Boolean hasRole;

  private String roleName;

  public UserRoleCheckResponse userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  
  @Schema(name = "userId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userId")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public UserRoleCheckResponse hasRole(Boolean hasRole) {
    this.hasRole = hasRole;
    return this;
  }

  /**
   * Indica si el usuario tiene el rol especificado
   * @return hasRole
  */
  
  @Schema(name = "hasRole", description = "Indica si el usuario tiene el rol especificado", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasRole")
  public Boolean getHasRole() {
    return hasRole;
  }

  public void setHasRole(Boolean hasRole) {
    this.hasRole = hasRole;
  }

  public UserRoleCheckResponse roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

  /**
   * Nombre del rol consultado
   * @return roleName
  */
  
  @Schema(name = "roleName", description = "Nombre del rol consultado", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleName")
  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRoleCheckResponse userRoleCheckResponse = (UserRoleCheckResponse) o;
    return Objects.equals(this.userId, userRoleCheckResponse.userId) &&
        Objects.equals(this.hasRole, userRoleCheckResponse.hasRole) &&
        Objects.equals(this.roleName, userRoleCheckResponse.roleName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, hasRole, roleName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRoleCheckResponse {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    hasRole: ").append(toIndentedString(hasRole)).append("\n");
    sb.append("    roleName: ").append(toIndentedString(roleName)).append("\n");
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

