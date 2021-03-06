package br.com.gbvbahia;

import br.com.gbvbahia.i18n.I18NTest;
import br.com.gbvbahia.maker.MakeEntitySynteticTest;
import br.com.gbvbahia.maker.MakeEntityTest;
import br.com.gbvbahia.maker.MakeEntitysTest;
import br.com.gbvbahia.maker.factories.SetupTest;
import br.com.gbvbahia.maker.onetoone.OneToOneTest;
import br.com.gbvbahia.maker.properties.MakePropertiesTest;
import br.com.gbvbahia.maker.types.date.MakeCalendarTest;
import br.com.gbvbahia.maker.types.date.MakeDateTest;
import br.com.gbvbahia.maker.types.primitives.common.MakeNumberTest;
import br.com.gbvbahia.maker.types.string.MakeCharacterTest;
import br.com.gbvbahia.maker.types.string.MakeStringTest;
import br.com.gbvbahia.maker.types.wrappers.MakeBooleanTest;
import br.com.gbvbahia.maker.types.wrappers.MakeByteTest;
import br.com.gbvbahia.maker.types.wrappers.MakeDoubleTest;
import br.com.gbvbahia.maker.types.wrappers.MakeFloatTest;
import br.com.gbvbahia.maker.types.wrappers.MakeIntegerTest;
import br.com.gbvbahia.maker.types.wrappers.MakeLongTest;
import br.com.gbvbahia.maker.types.wrappers.MakeShortTest;
import br.com.gbvbahia.maker.works.MakeBetweenTest;
import br.com.gbvbahia.maker.works.MakeCnpjTest;
import br.com.gbvbahia.maker.works.MakeCpfTest;
import br.com.gbvbahia.maker.works.MakeEmailTest;
import br.com.gbvbahia.maker.works.MakeInTest;
import br.com.gbvbahia.maker.works.MakeListTest;
import br.com.gbvbahia.maker.works.MakeSetTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @since v.1
 * @author Guilherme
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ I18NTest.class, MakeBooleanTest.class,
    MakeByteTest.class, MakeShortTest.class, MakeIntegerTest.class,
    MakeLongTest.class, MakeFloatTest.class, MakeDoubleTest.class,
    MakeCharacterTest.class, MakeStringTest.class, MakeCalendarTest.class,
    MakeDateTest.class, MakeCpfTest.class, MakeCnpjTest.class,
    MakeEntityTest.class, MakeEntitySynteticTest.class, MakeEntitysTest.class,
    MakePropertiesTest.class, MakeListTest.class, MakeSetTest.class,
    MakeBetweenTest.class, MakeInTest.class, OneToOneTest.class,
    MakeEmailTest.class, SetupTest.class, MakeEntitySynteticTest.class,
    MakeNumberTest.class })
public class Testes {
}
