/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.maker.factories.types;

import br.com.gbvbahia.i18n.I18N;
import br.com.gbvbahia.maker.factories.types.common.ValueFactory;
import br.com.gbvbahia.maker.wrappers.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory para classes anotadas com @Min e/ou @Max da JSR303.
 *
 * @since v.1 20/05/2012
 * @author Guilherme
 */
public class NumberFactory implements ValueFactory {

    /**
     * Logger para depuração.
     */
    private static Log logger = LogFactory.getLog("NumberFactory");

    @Override
    public <T> void makeValue(final Field f, final T entity)
            throws IllegalAccessException, IllegalArgumentException {
        insertValue(f, entity);
    }

    /**
     * Verifica o tipo do campo e insere o valor correspondente.
     *
     * @param <T> Generic que representa proprietário do campo. Por
     * exemplo: Classe carro tem um campo int rodas, o Field f seria
     * rodas e T seria a classe Carro que terá o campo definido.
     * @param f O campo que terá o valor definido.
     * @param toReturn Objeto que contém o Field.
     * @throws IllegalAccessException se no momento de execução não
     * houver acesso ao campo.
     * @throws IllegalArgumentException Se algum argumento anotado não
     * for válido.
     */
    private <T> void insertValue(Field f, T toReturn)
            throws IllegalAccessException, IllegalArgumentException {
        if (f.getType().equals(Integer.class)) {
            f.set(toReturn, valueToInteger(f));
        } else if (f.getType().equals(int.class)) {
            f.set(toReturn, valueToInteger(f).intValue());
        } else if (f.getType().equals(Long.class)) {
            f.set(toReturn, valueToLong(f));
        } else if (f.getType().equals(long.class)) {
            f.set(toReturn, valueToLong(f).longValue());
        } else if (f.getType().equals(Byte.class)) {
            f.set(toReturn, valueToByte(f));
        } else if (f.getType().equals(byte.class)) {
            f.set(toReturn, valueToByte(f).byteValue());
        } else if (f.getType().equals(Short.class)) {
            f.set(toReturn, valueToShort(f));
        } else if (f.getType().equals(short.class)) {
            f.set(toReturn, valueToShort(f).shortValue());
        } else if (f.getType().equals(Double.class)) {
            f.set(toReturn, valueToDouble(f));
        } else if (f.getType().equals(double.class)) {
            f.set(toReturn, valueToDouble(f).doubleValue());
        } else if (f.getType().equals(Float.class)) {
            f.set(toReturn, valueToFloat(f));
        } else if (f.getType().equals(float.class)) {
            f.set(toReturn, valueToFloat(f).floatValue());
        } else if (f.getType().equals(BigInteger.class)) {
            f.set(toReturn, valueToBigInteger(f));
        } else if (f.getType().equals(BigDecimal.class)) {
            f.set(toReturn, valueToBigDecimal(f));
        } else if (f.getType().equals(String.class)) {
            f.set(toReturn, valueToString(f));
        } else {
            throw new IllegalArgumentException(I18N.getMsg("tipoDesconhecidoMinMax"));
        }
    }

    /**
     * Cria um valor entre os valores anotados com
     * javax.validation.constraints.Min ou
     * javax.validation.constraints.Max da especificação JSR303.<br>
     * Se não encontrar os valores irá utilizar os determinados nos
     * parâmetros minValue e/ou MaxValue
     *
     * @param f Campo a ter o valor determinado.
     * @param minValue Mínimo aceitavel, será utilizado se não houver
     * a anotação @Min da JSR303.
     * @param maxValue Máximo aceitavel, será utilizado se não houver
     * a anotação @Max da JSR303.
     * @return Array de duas posições, [0] será o minimo e [1] o
     * máximo.
     */
    private static Long[] getMinMaxLongValues(Field f,
            Number minValue, Number maxValue) {
        Long[] toReturn = new Long[2];
        if (f.isAnnotationPresent(Min.class)) {
            toReturn[0] = new Long(f.getAnnotation(Min.class).value());
        } else {
            logger.debug(I18N.getMsg("defaultValue",
                    f.getType().getSimpleName()));
            toReturn[0] = minValue.longValue();
        }
        if (f.isAnnotationPresent(Max.class)) {
            toReturn[1] = new Long(f.getAnnotation(Max.class).value());
        } else {
            logger.debug(I18N.getMsg("defaultValue",
                    f.getType().getSimpleName()));
            toReturn[1] = maxValue.longValue();
        }
        return toReturn;
    }

    private static Integer valueToInteger(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Integer.MIN_VALUE,
                Integer.MAX_VALUE);
        int min = minMax[0].intValue();
        int max = minMax[1].intValue();
        return MakeInteger.getIntervalo(min, max);
    }

    private static Long valueToLong(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Long.MIN_VALUE,
                Long.MAX_VALUE);
        long min = minMax[0];
        long max = minMax[1];
        return MakeLong.getIntervalo(min, max);
    }

    private static String valueToString(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Long.MIN_VALUE,
                Long.MAX_VALUE);
        long min = minMax[0];
        long max = minMax[1];
        return MakeLong.getIntervalo(min, max).toString();
    }

    private static BigInteger valueToBigInteger(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Long.MIN_VALUE,
                Long.MAX_VALUE);
        long min = minMax[0];
        long max = minMax[1];
        return new BigInteger(new Long(MakeLong.getIntervalo(min,
                max)).toString());
    }

    private static BigDecimal valueToBigDecimal(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Long.MIN_VALUE,
                Long.MAX_VALUE);
        long min = minMax[0];
        long max = minMax[1];
        return new BigDecimal(new Double(MakeDouble.getIntervalo(min,
                max)).toString());
    }

    private static Double valueToDouble(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Long.MIN_VALUE,
                Long.MAX_VALUE);
        long min = minMax[0];
        long max = minMax[1];
        return MakeDouble.getIntervalo(min, max);
    }

    private static Float valueToFloat(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Float.MIN_VALUE,
                Float.MAX_VALUE);
        long min = minMax[0];
        long max = minMax[1];
        return MakeDouble.getIntervalo(min, max).floatValue();
    }

    private static Byte valueToByte(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Byte.MIN_VALUE,
                Byte.MAX_VALUE);
        byte min = minMax[0].byteValue();
        byte max = minMax[1].byteValue();
        return MakeByte.getIntervalo(min, max);
    }

    private static Short valueToShort(Field f) {
        Long[] minMax = getMinMaxLongValues(f, Short.MIN_VALUE,
                Short.MAX_VALUE);
        short min = minMax[0].shortValue();
        short max = minMax[1].shortValue();
        return MakeShort.getIntervalo(min, max);
    }
}
