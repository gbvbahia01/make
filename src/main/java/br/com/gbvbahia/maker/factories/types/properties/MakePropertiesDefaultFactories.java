/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.maker.factories.types.properties;

import br.com.gbvbahia.i18n.I18N;
import br.com.gbvbahia.maker.log.LogInfo;
import br.com.gbvbahia.maker.works.*;
import br.com.gbvbahia.maker.works.common.ValuePropertiesFactory;
import br.com.gbvbahia.maker.works.execeptions.MakeWorkException;
import java.util.ArrayList;
import java.util.List;

/**
 * Todas as classes default devem ser declaradas aqui, adicionadas na
 * lista WORK_FACTORIES no corpo estático.
 *
 * @since v.1 09/06/2012
 * @author Guilherme
 */
public class MakePropertiesDefaultFactories {

    final static List<Class<? extends ValuePropertiesFactory>> WORK_FACTORIES =
            new ArrayList<Class<? extends ValuePropertiesFactory>>();

    static {
        WORK_FACTORIES.add(MakeCPF.class);
        WORK_FACTORIES.add(MakeCNPJ.class);
        WORK_FACTORIES.add(MakeName.class);
        WORK_FACTORIES.add(MakeEmail.class);
        WORK_FACTORIES.add(MakeList.class);
        WORK_FACTORIES.add(MakeSet.class);
        WORK_FACTORIES.add(MakeBetween.class);
    }

    /**
     * Verifica se os default são necessários ao teste, carrega
     * somente se houver necessidade.<br> Os implementados pelo
     * desenvolvedor tem preferência sobre os default.
     *
     * @param value Valor declarado no properties pelo usuário.
     * @return A fabrica personalizada de valores, implementada pelo
     * desenvolvedor ou default do Make.
     */
    static ValuePropertiesFactory getPropertiesFactory(String value) {
        for (int i = 0; i < WORK_FACTORIES.size(); i++) {
            try {
                ValuePropertiesFactory vpf = WORK_FACTORIES.get(i).newInstance();
                if (vpf.workValue(value)) {
                    return vpf;
                }
            } catch (MakeWorkException ex) {
                LogInfo.logErrorInformation(ex.getClassOrigem(),
                        I18N.getMsg(ex.getMsgProperties(),
                        ex.getVariations()), ex.getCause());
                ex.printStackTrace();
                throw ex;
            } catch (InstantiationException ex) {
                LogInfo.logWarnInformation("MakeDefaultFactories",
                        I18N.getMsg("propertiesFactoryInstantiationException",
                        WORK_FACTORIES.get(i).getSimpleName()));
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                LogInfo.logWarnInformation("MakeDefaultFactories",
                        I18N.getMsg("propertiesFactoryIllegalAccessException",
                        WORK_FACTORIES.get(i).getSimpleName()));
                ex.printStackTrace();
            } catch (IllegalArgumentException ex) {
                LogInfo.logWarnInformation("MakeDefaultFactories",
                        I18N.getMsg("propertiesFactoryIllegalArgumentException",
                        WORK_FACTORIES.get(i).getSimpleName(), value));
                ex.printStackTrace();
            } catch (SecurityException ex) {
                LogInfo.logWarnInformation("MakeDefaultFactories",
                        I18N.getMsg("propertiesFactorySecurityException",
                        WORK_FACTORIES.get(i).getSimpleName()));
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Carrega as classes da fabricas personalizadas implementadas
     * pelo desenvolvedor.
     *
     * @param key
     * @param value
     */
    static void insertImplFactory(final String key, final String value) {
        try {
            Class<? extends ValuePropertiesFactory> fac =
                    (Class<? extends ValuePropertiesFactory>) Class.forName(value);
            if (!WORK_FACTORIES.contains(fac)) {
                WORK_FACTORIES.add(0, fac);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            LogInfo.logErrorInformation("MakePropertiesDefaultFactories",
                    I18N.getMsg("workUserNotFound", value), ex);
        }
    }
}
