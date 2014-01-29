package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.HashMap;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

public class DomainSpecificForm
    extends Form<Void>
{
    private static final long serialVersionUID = -2244336369430716569L;

    public DomainSpecificForm(final String id)
    {
        super(id);
    }

    protected PropertyModel<Integer> createIntProperty(final String property)
    {
        return this.createProperty(property, Integer.class);
    }

    protected PropertyModel<String> createStringProperty(final String property)
    {
        return this.createProperty(property, String.class);
    }

    protected PropertyModel<Long> createLongProperty(final String property)
    {
        return this.createProperty(property, Long.class);
    }

    protected PropertyModel<Boolean> createBooleanProperty(final String property)
    {
        return this.createProperty(property, Boolean.class);
    }

    protected <T> PropertyModel<T> createProperty(final String property, final Class<T> type)
    {
        return new PropertyModel<T>(this, property);
    }

    protected HashMap<String, Object> createInitializationParameters()
    {
        return new HashMap<String, Object>();
    }
}
