package school.cesar.eta.unit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith({ MockitoExtension.class })
public class PersonTest {

	Person person;
	PersonMock personMock;

	@InjectMocks
	Person personFamily = new Person();
	
	@InjectMocks
	Person personFamily2 = new Person();

	@Mock
	List<Person> family = new ArrayList<Person>();

	@BeforeEach
	public void createPerson() {
		person = new Person() {
			@Override
			public LocalDate getNow() {
				return LocalDate.of(2020, 9, 30);
			}
		};
	}

	@Test
	public void getName_firstNameJonLastNameSnow_jonSnow() {
		person.setName("Jon");
		person.setLastName("Snow");
		Assertions.assertEquals("JonSnow", person.getName());
	}

	@Test
	public void getName_firstNameJonNoLastName_jon() {
		person.setName("Jon");
		Assertions.assertEquals("Jon", person.getName());
	}

	@Test
	public void getName_noFirstNameLastNameSnow_snow() {
		person.setLastName("Snow");
		Assertions.assertEquals("Snow", person.getName());
	}

	@Test
	public void getName_noFirstNameNorLastName_throwsException() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			person.getName();
		});
	}

	@Test
	public void isBirthdayToday_differentMonthAndDay_false() {
		person.setBirthday(LocalDate.of(2020, 12, 31));
		Assertions.assertFalse(person.isBirthdayToday());
	}

	@Test
	public void isBirthdayToday_sameMonthDifferentDay_false() {
		person.setBirthday(LocalDate.of(2020, 9, 29));
		Assertions.assertFalse(person.isBirthdayToday());
	}

	@Test
	public void isBirthdayToday_sameMonthAndDay_true() {
		person.setBirthday(LocalDate.of(2020, 9, 30));
		Assertions.assertTrue(person.isBirthdayToday());
	}

	// teste extra para cobrir o metodo isBirthdayToday
	@Test
	public void isBirthdayToday_differentMonthSameDay_false() {
		person.setBirthday(LocalDate.of(2020, 8, 30));
		Assertions.assertFalse(person.isBirthdayToday());
	}

	@Test
	public void addToFamily_somePerson_familyHasNewMember() {
		personMock = new PersonMock();
		personMock.addToFamily(personFamily);
		Assertions.assertTrue(personMock.family.contains(personFamily));
	}

	@Test
	public void addToFamily_somePerson_personAddedAlsoHasItsFamilyUpdated() {
		personFamily.addToFamily(personFamily2);
        verify(family, times(1)).add(personFamily);
	}

	@Test
	public void isFamily_nonRelativePerson_false() {
		Person personNonRelative = new Person();
		Assertions.assertFalse(person.isFamily(personNonRelative));
	}

	@Test
	public void isFamily_relativePerson_true() {
		when(family.contains(personFamily2)).thenReturn(true);
        Assertions.assertTrue(personFamily.isFamily(personFamily2));
	}
}
