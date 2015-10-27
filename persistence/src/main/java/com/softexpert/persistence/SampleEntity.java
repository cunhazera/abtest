package com.softexpert.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SampleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	@NotNull(message = "Nome do tributo não pode ser nulo")
	@Size(min = 1, message = "Nome do tributo deve conter letras, exemplo IPI")
	public String name;

}
