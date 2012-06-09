/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.maker.factories.types;

import br.com.gbvbahia.i18n.I18N;
import br.com.gbvbahia.maker.MakeEntity;
import br.com.gbvbahia.maker.log.LogInfo;
import br.com.gbvbahia.maker.types.string.MakeCharacter;
import br.com.gbvbahia.maker.types.string.MakeString;
import br.com.gbvbahia.maker.types.wrappers.MakeBoolean;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

/**
 * Deve ser utilizado como <b>Factory Padrão</b>, para atributos
 * somente anotados com @NotNull.<br> Extende NumberFactory, por já
 * ter implementação de valores default para algumas classes.
 *
 * @since v.1 20/05/2012
 * @author Guilherme
 */
public class DefaultFactory extends NumberFactory {

    /**
     * Representa um map com chave class para a classe de objetos
     * criados e os objetos criados como valor, podendo reaproveitar
     * em relacionamentos.
     */
    private static Map<Class, Object> criados = new HashMap<Class, Object>();
    /**
     * Garante a quantidade de recursividade, se passar de 5 irá
     * reaproveitar objetos.
     */
    private static int recursiveCount = 0;
    private String className = this.getClass().getSimpleName();

    @Override
    public <T> void makeValue(Field f, T entity, boolean makeRelationships)
            throws IllegalAccessException, IllegalArgumentException {
        criados.put(entity.getClass(), entity);
        if (f.getType().equals(String.class)) {
            LogInfo.logDefaultValue(entity, f, className);
            f.set(entity,
                    MakeString.getString(MakeString.MIN_LENGTH_DEFAULT,
                    MakeString.MAX_LENGTH_DEFAULT,
                    MakeString.StringType.LETTER));
            return;
        }
        try {
            super.makeValue(f, entity, makeRelationships);
        } catch (IllegalArgumentException ex) {
            if (MakeCharacter.isCharacter(f)) {
                valueToCharacter(f, entity);
            } else if (MakeBoolean.isBoolean(f)) {
                valueToBoolean(f, entity);
            } else if (isDate(f)) {
                LogInfo.logDefaultValue(entity, f, className);
                new DateFactory().makeValue(f, entity, makeRelationships);
            } else if (makeRelationships
                    && f.getType().isAnnotationPresent(Entity.class)) {
                avoidCyclicReference(f, entity, makeRelationships);
                recursiveCount--;
            } else {
                throw new IllegalArgumentException(I18N.getMsg("tipoDesconhecidoDefault"));
            }
        }
    }

    private <T> void valueToBoolean(Field f, T entity)
            throws IllegalArgumentException, IllegalAccessException {
        LogInfo.logDefaultValue(entity, f, className);
        if (f.getType().equals(Boolean.class)) {
            f.set(entity, MakeBoolean.getBoolean());
        } else {
            f.set(entity, MakeBoolean.getBoolean().booleanValue());
        }
    }

    private <T> void valueToCharacter(Field f, T entity)
            throws IllegalArgumentException, IllegalAccessException {
        LogInfo.logDefaultValue(entity, f, className);
        if (f.getType().equals(Character.class)) {
            f.set(entity, MakeCharacter.getCharacter());
        } else {
            f.set(entity, MakeCharacter.getCharacter().charValue());
        }
    }

    /**
     * Evita referência ciclica, ou um loop infinito.
     *
     * @param <T> Tipo da entidade com fields definidos.
     * @param f Field a ser definido.
     * @param entity Entidade que está sendo definida.
     * @param makeRelationships Define se é para fazer objetos de
     * relacionamentos.
     * @throws IllegalAccessException Se não conseguir definir valor
     * em um field.
     * @throws IllegalArgumentException Se o field não puder ser
     * construído devido algum relacionamento incorreto.
     */
    private <T> void avoidCyclicReference(Field f, T entity,
            boolean makeRelationships) throws IllegalAccessException,
            IllegalArgumentException {
        if (recursiveCount > 3
                && criados.containsKey(f.getType())) {
            f.set(entity, criados.get(f.getType()));
            LogInfo.logWarnInformation(className,
                    I18N.getMsg("possivelReferenciaCiclica",
                    f.getType().getSimpleName(),
                    entity.getClass().getSimpleName()));
        } else if (isMappedBy(f) && criados.containsKey(f.getType())) {
            f.set(entity, criados.get(f.getType()));
            LogInfo.logInfoInformation(className,
                    I18N.getMsg("mappedByDetected",
                    entity.getClass().getSimpleName(),
                    f.getName(),
                    criados.get(f.getType()).getClass().getSimpleName()));
        } else {
            recursiveCount++;
            criados.put(f.getType(),
                    MakeEntity.makeEntity(f.getType(),
                    makeRelationships));
            f.set(entity, criados.get(f.getType()));
        }
    }

    /**
     * Verifica se o field é do tipo Date ou Calendar.
     *
     * @param f Atributo da classe.
     * @return True para Calendar ou Date, false se não for.
     */
    private boolean isDate(Field f) {
        if (f.getType().equals(Date.class)
                || f.getType().equals(Calendar.class)) {
            return true;
        }
        return false;
    }

    /**
     * Verifica se existe o mapeamento mappedBy da JPA, que significa
     * que o objeto do outro lado que é proprietário do
     * relacionamento.
     *
     * @param f Field a ser populado.
     * @return True para se tiver atributo mappedBy false para não.
     */
    private boolean isMappedBy(Field f) {
        if (f.isAnnotationPresent(OneToOne.class)) {
            return f.getAnnotation(OneToOne.class).mappedBy() != null;
        } else {
            return false;
        }
    }
}
