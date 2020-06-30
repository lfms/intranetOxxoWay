/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Charolastros
 */

@Repository
public class DaoTest {
    
    Log log =  LogFactory.getLog(this.getClass());
    public void test(){
        System.out.println("com.blitz.adminpago.dao.DaoTest.test()");
        log.info("log desde el DAO TEST");
    }
    
}
