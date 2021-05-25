package ejb.session.stateless;

import entity.Program;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
//import org.junit.After;
//import org.junit.AfterClass;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
import util.exception.CreateNewProgramException;
import util.exception.InputDataValidationException;
import util.exception.ProgramTitleExistException;
import util.exception.UnknownPersistenceException;


@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ProgramSessionBeanTest {

    private static ProgramSessionBeanLocal programSessionBeanLocal;
    
    public ProgramSessionBeanTest()
    {
    }    

    @BeforeAll
    public static void setUpClass() 
    {
        programSessionBeanLocal = lookupProgramSessionBeanLocal();
    }

    @AfterAll
    public static void tearDownClass() 
    {
    }

    @BeforeEach
    public void setUp() 
    {
    }

    @AfterEach
    public void tearDown() 
    {
    }

    @Test
    public void test01CreateProgram01() throws ProgramTitleExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProgramException, ParseException
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program expectedProgram = new Program("Title 1", "Description 1", date, date2);
        Long[] temp = new Long[1];
        temp[0] = 1l;
        List<Long> userIds = Arrays.asList(temp);
        //userIds.add(1l);
        Long actualProgramId = programSessionBeanLocal.createProgram(expectedProgram, 1l, userIds);
        
        Assertions.assertNotNull(actualProgramId);
        Assertions.assertEquals(1l, actualProgramId.longValue());
    }

    @Test
    public void test02CreateProgram02() throws ProgramTitleExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProgramException, ParseException
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program expectedProgram = new Program("Title 1", "Description 1", date, date2);
        Long[] temp = new Long[1];
        temp[0] = 1l;
        List<Long> userIds = Arrays.asList(temp);
        //Long actualProgramId = programSessionBeanLocal.createProgram(expectedProgram, 1l, userIds);
        Assertions.assertThrows(ProgramTitleExistException.class, () -> programSessionBeanLocal.createProgram(expectedProgram, 1l, userIds));
    }

    @Test
    public void test03CreateProgram03() throws ProgramTitleExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProgramException, ParseException
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program expectedProgram = new Program("Title 1", "Description 1", date, date2);
        Long[] temp = new Long[1];
        temp[0] = 1l;
        List<Long> userIds = Arrays.asList(temp);
        //Long actualProgramId = programSessionBeanLocal.createProgram(expectedProgram, 1l, userIds);
        Assertions.assertThrows(InputDataValidationException.class, () -> programSessionBeanLocal.createProgram(expectedProgram, 1l, userIds));
    }

    @Test
    public void test04CreateProgram04() throws ProgramTitleExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProgramException, ParseException
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program expectedProgram = new Program("Title 1", "Description 1", date, date2);
        Long[] temp = new Long[1];
        temp[0] = 1l;
        List<Long> userIds = Arrays.asList(temp);
        //Long actualProgramId = programSessionBeanLocal.createProgram(expectedProgram, 2l, userIds);
        Assertions.assertThrows(CreateNewProgramException.class, () -> programSessionBeanLocal.createProgram(expectedProgram, 2l, userIds));
    }
 
    @Test
    public void test05CreateProgram05() throws ProgramTitleExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProgramException, ParseException
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program expectedProgram = new Program("Title 1", "Description 1", date, date2);
        //Long actualProgramId = programSessionBeanLocal.createProgram(expectedProgram, null, null);
        Assertions.assertThrows(CreateNewProgramException.class, () -> programSessionBeanLocal.createProgram(expectedProgram, null, null));
    }
          

    private static ProgramSessionBeanLocal lookupProgramSessionBeanLocal() {
        try {
            Context c = (Context) new InitialContext();
            return (ProgramSessionBeanLocal) c.lookup("java:global/ChallengeTrackerApplication/ChallengeTrackerApplication-ejb/ProgramSessionBean!ejb.session.stateless.ProgramSessionBeanLocal");
        } catch (NamingException ne) {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}

