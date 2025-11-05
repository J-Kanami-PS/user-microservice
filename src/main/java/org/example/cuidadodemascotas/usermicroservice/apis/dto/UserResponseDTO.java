package org.example.cuidadodemascotas.usermicroservice.apis.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleResponseDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserResponseDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-05T00:02:41.133339100-03:00[America/Asuncion]")
public class UserResponseDTO {

  private Long id;

  private String name;

  private String lastName;

  private String email;

  private String phoneNumber;

  private String profilePhoto;

  /**
   * Gets or Sets availabilityState
   */
  public enum AvailabilityStateEnum {
    AVAILABLE("AVAILABLE"),
    
    NOT_AVAILABLE("NOT_AVAILABLE"),
    
    BUSY("BUSY");

    private String value;

    AvailabilityStateEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AvailabilityStateEnum fromValue(String value) {
      for (AvailabilityStateEnum b : AvailabilityStateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AvailabilityStateEnum availabilityState;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  private Boolean active;

  @Valid
  private List<@Valid RoleResponseDTO> roles;

  public UserResponseDTO id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserResponseDTO name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserResponseDTO lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
  */
  
  @Schema(name = "lastName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UserResponseDTO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserResponseDTO phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  /**
   * Get phoneNumber
   * @return phoneNumber
  */
  
  @Schema(name = "phoneNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phoneNumber")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public UserResponseDTO profilePhoto(String profilePhoto) {
    this.profilePhoto = profilePhoto;
    return this;
  }

  /**
   * Get profilePhoto
   * @return profilePhoto
  */
  
  @Schema(name = "profilePhoto", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profilePhoto")
  public String getProfilePhoto() {
    return profilePhoto;
  }

  public void setProfilePhoto(String profilePhoto) {
    this.profilePhoto = profilePhoto;
  }

  public UserResponseDTO availabilityState(AvailabilityStateEnum availabilityState) {
    this.availabilityState = availabilityState;
    return this;
  }

  /**
   * Get availabilityState
   * @return availabilityState
  */
  
  @Schema(name = "availabilityState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availabilityState")
  public AvailabilityStateEnum getAvailabilityState() {
    return availabilityState;
  }

  public void setAvailabilityState(AvailabilityStateEnum availabilityState) {
    this.availabilityState = availabilityState;
  }

  public UserResponseDTO createdAt(OffsetDateTime createdAt) {
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

  public UserResponseDTO updatedAt(OffsetDateTime updatedAt) {
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

  public UserResponseDTO active(Boolean active) {
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

  public UserResponseDTO roles(List<@Valid RoleResponseDTO> roles) {
    this.roles = roles;
    return this;
  }

  public UserResponseDTO addRolesItem(RoleResponseDTO rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get roles
   * @return roles
  */
  @Valid 
  @Schema(name = "roles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roles")
  public List<@Valid RoleResponseDTO> getRoles() {
    return roles;
  }

  public void setRoles(List<@Valid RoleResponseDTO> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserResponseDTO userResponseDTO = (UserResponseDTO) o;
    return Objects.equals(this.id, userResponseDTO.id) &&
        Objects.equals(this.name, userResponseDTO.name) &&
        Objects.equals(this.lastName, userResponseDTO.lastName) &&
        Objects.equals(this.email, userResponseDTO.email) &&
        Objects.equals(this.phoneNumber, userResponseDTO.phoneNumber) &&
        Objects.equals(this.profilePhoto, userResponseDTO.profilePhoto) &&
        Objects.equals(this.availabilityState, userResponseDTO.availabilityState) &&
        Objects.equals(this.createdAt, userResponseDTO.createdAt) &&
        Objects.equals(this.updatedAt, userResponseDTO.updatedAt) &&
        Objects.equals(this.active, userResponseDTO.active) &&
        Objects.equals(this.roles, userResponseDTO.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, lastName, email, phoneNumber, profilePhoto, availabilityState, createdAt, updatedAt, active, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserResponseDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    profilePhoto: ").append(toIndentedString(profilePhoto)).append("\n");
    sb.append("    availabilityState: ").append(toIndentedString(availabilityState)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

