/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entity.Milestone;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import util.exception.CreateNewMilestoneException;
import util.exception.InputDataValidationException;
import util.exception.MilestoneTitleExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author sthit
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MilestoneSessionBeanTest {

    private static MilestoneSessionBeanLocal milestoneSessionBean;
    
    
    
    public MilestoneSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        milestoneSessionBean = lookupMilestoneSessionBeanLocal();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void test01CreateMilestone01() throws ParseException, MilestoneTitleExistException, UnknownPersistenceException, CreateNewMilestoneException, InputDataValidationException 
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Milestone expectedMilestone = new Milestone("Title 1", "Description 1", "Program", date, date2, new BigDecimal(60), new BigDecimal(58),"Health", "Weight", 10);
        Long actualMilestoneId = milestoneSessionBean.createMilestone(expectedMilestone, 1l);
        
        assertNotNull(actualMilestoneId);
        assertEquals(1l, actualMilestoneId.longValue());        
    }
    
    @Test(expected = MilestoneTitleExistException.class)
    public void test02CreateMilestone02() throws ParseException, MilestoneTitleExistException, UnknownPersistenceException, CreateNewMilestoneException, InputDataValidationException 
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Milestone expectedMilestone = new Milestone("Title 1", "Description 1", "Program", date, date2, new BigDecimal(60), new BigDecimal(58), "Health", "Weight", 10);
        Long actualMilestoneId = milestoneSessionBean.createMilestone(expectedMilestone, 1l);    
    }
 
    @Test(expected = InputDataValidationException.class)
    public void test03CreateMilestone03() throws ParseException, MilestoneTitleExistException, UnknownPersistenceException, CreateNewMilestoneException, InputDataValidationException 
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Milestone expectedMilestone = new Milestone("T1", "Description 1", "Program", date, date2, new BigDecimal(60), new BigDecimal(58), "Health", "Weight", 10);
        Long actualMilestoneId = milestoneSessionBean.createMilestone(expectedMilestone, 1l);
        
    }
    
    @Test(expected = CreateNewMilestoneException.class)
    public void test04CreateMilestone04() throws ParseException, MilestoneTitleExistException, UnknownPersistenceException, CreateNewMilestoneException, InputDataValidationException 
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Milestone expectedMilestone = new Milestone("Title 1", "Description 1", "Program", date, date2, new BigDecimal(60), new BigDecimal(58), "Health", "Weight", 10);
        Long actualMilestoneId = milestoneSessionBean.createMilestone(expectedMilestone, 3l);   
    }
    
    private static MilestoneSessionBeanLocal lookupMilestoneSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (MilestoneSessionBeanLocal) c.lookup("java:global/ChallengeTrackerApplication/ChallengeTrackerApplication-ejb/MilestoneSessionBean!ejb.session.stateless.MilestoneSessionBeanLocal");
        } catch (NamingException ne) {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
