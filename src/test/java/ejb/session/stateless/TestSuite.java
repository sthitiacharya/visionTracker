/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

//import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@ExtendWith(JUnitPlatform.class)
@Suite.SuiteClasses({ProgramSessionBeanTest.class,
    MilestoneSessionBeanTest.class,
    NewTest.class})

public class TestSuite 
{
    @BeforeAll
    public static void setUpClass() throws Exception 
    {
    }

    
    
    @AfterAll
    public static void tearDownClass() throws Exception 
    {
    }

    
    
    @BeforeEach
    public void setUp() throws Exception 
    {
    }

    
    
    @AfterEach
    public void tearDown() throws Exception 
    {
    }    
}