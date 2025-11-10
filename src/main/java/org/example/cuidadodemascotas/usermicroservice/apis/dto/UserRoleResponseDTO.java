package org.example.cuidadodemascotas.usermicroservice.apis.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserRoleResponseDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-09T21:17:03.821965500-03:00[America/Asuncion]")
public class UserRoleResponseDTO {

  private Long id;

  private Long userId;

  private Long roleId;

  private String roleName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  private Boolean active;

  public UserRoleResponseDTO id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * ID de la relación usuario-rol
   * @return id
  */
  
  @Schema(name = "id", description = "ID de la relación usuario-rol", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserRoleResponseDTO userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * ID del usuario
   * @return userId
  */
  
  @Schema(name = "userId", description = "ID del usuario", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userId")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public UserRoleResponseDTO roleId(Long roleId) {
    this.roleId = roleId;
    return this;
  }

  /**
   * ID del rol
   * @return roleId
  */
  
  @Schema(name = "roleId", description = "ID del rol", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleId")
  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public UserRoleResponseDTO roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

  /**
   * Nombre del rol asignado
   * @return roleName
  */
  
  @Schema(name = "roleName", description = "Nombre del rol asignado", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleName")
  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public UserRoleResponseDTO createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
  */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public UserRoleResponseDTO updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
  */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public UserRoleResponseDTO active(Boolean active) {
    this.active = active;
    return this;
  }

  /**
   * Get active
   * @return active
  */
  
  @Schema(name = "active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active")
  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRoleResponseDTO userRoleResponseDTO = (UserRoleResponseDTO) o;
    return Objects.equals(this.id, userRoleResponseDTO.id) &&
        Objects.equals(this.userId, userRoleResponseDTO.userId) &&
        Objects.equals(this.roleId, userRoleResponseDTO.roleId) &&
        Objects.equals(this.roleName, userRoleResponseDTO.roleName) &&
        Objects.equals(this.createdAt, userRoleResponseDTO.createdAt) &&
        Objects.equals(this.updatedAt, userRoleResponseDTO.updatedAt) &&
        Objects.equals(this.active, userRoleResponseDTO.active);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, roleId, roleName, createdAt, updatedAt, active);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRoleResponseDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
    sb.append("    roleName: ").append(toIndentedString(roleName)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
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

