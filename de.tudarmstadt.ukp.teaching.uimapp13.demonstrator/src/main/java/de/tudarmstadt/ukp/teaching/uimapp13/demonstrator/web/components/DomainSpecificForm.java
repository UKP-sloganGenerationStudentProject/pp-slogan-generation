package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.Adapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.HomePage;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.SessionAttributes;

public abstract class DomainSpecificForm
    extends Form<Void>
{
    /**
     * Holds the most recent generation configuration
     */
    protected HashMap<Object, Object> previousConfiguration;

    protected Logger logger;

    private static final long serialVersionUID = -2244336369430716569L;

    private Adapter adapter;

    public DomainSpecificForm(final String id)
    {
        super(id);

        this.logger = LoggerFactory.getLogger(this.getClass());

        final Serializable sessionGenerationConfig = this.getSession().getAttribute(
                SessionAttributes.CONFIG);
        this.previousConfiguration = new HashMap<>();
        if (sessionGenerationConfig != null) {
            @SuppressWarnings("unchecked")
            final HashMap<String, Object> cachedConfig = (HashMap<String, Object>) sessionGenerationConfig;
            this.previousConfiguration.putAll(cachedConfig);
        }

        this.initializeDefaultValues();

        if (this.loadAdapterEagerly()) {
            this.initializeAdapter();
        }
    }

    @Override
    public void onSubmit()
    {
        if (this.loadAdapterLazily()) {
            this.initializeAdapter();
        }

        final HashMap<String, Object> parameters = this.createGenerationParameters();
        this.getSession().setAttribute(SessionAttributes.CONFIG, parameters);

        final List<Slogan> slogans = new ArrayList<>();
        String statusMessage;
        try {
            this.logger.info("Generating slogans...");
            this.logger.info("Generation parameters: " + parameters);

            slogans.addAll(this.adapter.generateSlogans(parameters));

            this.logger.info("Generating slogans...Done");

            statusMessage = "";
        }
        catch (final Exception e) {
            e.printStackTrace();
            statusMessage = e.getMessage();
        }

        this.getSession().setAttribute(SessionAttributes.DOMAIN, this.adapter.getDomain());

        this.setResponsePage(new HomePage(slogans, statusMessage));
    }

    private void initializeAdapter()
    {
        this.adapter = this.createAdapter();
        final String adapterName = this.adapter.getClass().getSimpleName();
        this.logger.info("Initializing " + adapterName);
        try {
            final Map<String, Object> initParameters = this.createInitializationParameters();
            this.logger.info("Initialization parameters: " + initParameters);
            this.adapter.initialize(initParameters);
        }
        catch (final Exception e) {
            throw new IllegalStateException(e);
        }
        this.logger.info("Initializing " + this.adapter.getClass().getSimpleName() + "...Done");
    }

    /**
     * Within this method, the default settings are initialized
     */
    protected abstract void initializeDefaultValues();

    /**
     * Returns an instance of the adapter to be used.
     * The adapter need not be initialized.
     * 
     * @return the adapter to be used
     */
    protected abstract Adapter createAdapter();

    /**
     * Always re-initializes the adapter for each query
     * 
     * @return whether the adapter is re-initialized every time
     */
    protected boolean loadAdapterEagerly()
    {
        return false;
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

    protected HashMap<String, Object> createGenerationParameters()
    {
        return new HashMap<String, Object>();
    }

    @SuppressWarnings("unchecked")
    protected <T> T getParam(final String key, final T defaultValue)
    {
        T result = defaultValue;
        if (this.previousConfiguration.containsKey(key)) {
            final Object value = this.previousConfiguration.get(key);
            try {
                result = (T) value;
            }
            catch (final ClassCastException ex) {
                this.logger
                        .warn(String
                                .format("Value for parameter %s has not the expected type %s, instead: '%s'. Using default value %s instead.",
                                        key, defaultValue.getClass().getSimpleName(), value,
                                        defaultValue.toString()));
            }
        }
        return result;
    }

    private boolean loadAdapterLazily()
    {
        return !this.loadAdapterEagerly();
    }
}
