package org.example.cuidadodemascotas.usermicroservice.apis.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserRequestDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-20T14:43:02.908640100-03:00[America/Asuncion]")
public class UserRequestDTO {

  private String name;

  private String lastName;

  private String email;

  private String password;

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

  private AvailabilityStateEnum availabilityState = AvailabilityStateEnum.AVAILABLE;

  public UserRequestDTO() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserRequestDTO(String name, String lastName, String email, String password, String phoneNumber, AvailabilityStateEnum availabilityState) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.availabilityState = availabilityState;
  }

  public UserRequestDTO name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull @Size(max = 60) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserRequestDTO lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
  */
  @NotNull @Size(max = 60) 
  @Schema(name = "lastName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UserRequestDTO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @NotNull @Size(max = 100) @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserRequestDTO password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
  */
  @NotNull 
  @Schema(name = "password", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserRequestDTO phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  /**
   * Get phoneNumber
   * @return phoneNumber
  */
  @NotNull @Pattern(regexp = "^[0-9]{10,15}$") 
  @Schema(name = "phoneNumber", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("phoneNumber")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public UserRequestDTO profilePhoto(String profilePhoto) {
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

  public UserRequestDTO availabilityState(AvailabilityStateEnum availabilityState) {
    this.availabilityState = availabilityState;
    return this;
  }

  /**
   * Get availabilityState
   * @return availabilityState
  */
  @NotNull 
  @Schema(name = "availabilityState", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("availabilityState")
  public AvailabilityStateEnum getAvailabilityState() {
    return availabilityState;
  }

  public void setAvailabilityState(AvailabilityStateEnum availabilityState) {
    this.availabilityState = availabilityState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRequestDTO userRequestDTO = (UserRequestDTO) o;
    return Objects.equals(this.name, userRequestDTO.name) &&
        Objects.equals(this.lastName, userRequestDTO.lastName) &&
        Objects.equals(this.email, userRequestDTO.email) &&
        Objects.equals(this.password, userRequestDTO.password) &&
        Objects.equals(this.phoneNumber, userRequestDTO.phoneNumber) &&
        Objects.equals(this.profilePhoto, userRequestDTO.profilePhoto) &&
        Objects.equals(this.availabilityState, userRequestDTO.availabilityState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, lastName, email, password, phoneNumber, profilePhoto, availabilityState);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRequestDTO {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    profilePhoto: ").append(toIndentedString(profilePhoto)).append("\n");
    sb.append("    availabilityState: ").append(toIndentedString(availabilityState)).append("\n");
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

