package br.com.gbvbahia.maker;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.junit.Test;

import br.com.gbvbahia.entities.EntitySyntheticTest;
import br.com.gbvbahia.maker.factories.Factory;
import br.com.gbvbahia.maker.log.LogInfo;

/**
 * @since v.1 01/05/2012
 * @author Guilherme
 */
public class MakeEntitySynteticTest extends TestCase {

  private static Log logger = LogInfo.getLog("Test :: MakeEntitySynteticTest");

  public MakeEntitySynteticTest() {
    super("Make Syntetic Entity");
  }

  @Test
  public void testMakeSyntetic() throws Exception {
    Factory.loadSetup("make.xml");
    logger.info("Entity - MakeSyntetic");
    EntitySyntheticTest test = MakeEntity.makeEntity(EntitySyntheticTest.class);
    assertNotNull("Test é nulo.", test);
    assertNotNull("Ref Ciclica nula.", test.getCicleTest());
    assertNotNull("Entidade NotNullTest nula", test.getNotNullTest());
    assertNotNull("NotEntity é nula.", test.getNoEntity());
    logger.debug(test);
  }
}
