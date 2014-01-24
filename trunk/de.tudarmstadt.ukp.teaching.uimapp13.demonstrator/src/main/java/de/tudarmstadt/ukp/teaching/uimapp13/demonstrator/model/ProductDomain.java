package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ProductDomain
    implements Serializable
{
    private static final long serialVersionUID = 5608530679368325726L;

    public static final ProductDomain BEAUTY = new ProductDomain("beauty", "Cosmetics");
    public static final ProductDomain CARS = new ProductDomain("cars", "Cars");
    public static final ProductDomain GAMES = new ProductDomain("games", "Games");
    public static final ProductDomain SOFT_DRINKS = new ProductDomain("soft", "Soft Drinks");

    private static final List<ProductDomain> ALL_DOMAINS = Arrays.asList(BEAUTY, CARS, GAMES,
            SOFT_DRINKS);

    private final String id;
    private final String name;

    public ProductDomain(final String id, final String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public static List<ProductDomain> getAllDomains()
    {
        return ALL_DOMAINS;
    }
}
