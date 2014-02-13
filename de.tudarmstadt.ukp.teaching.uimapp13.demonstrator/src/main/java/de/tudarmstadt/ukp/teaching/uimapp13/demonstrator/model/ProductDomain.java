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

    private static final List<ProductDomain> ALL_DOMAINS = Arrays.asList(BEAUTY, CARS, GAMES);

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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final ProductDomain other = (ProductDomain) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
