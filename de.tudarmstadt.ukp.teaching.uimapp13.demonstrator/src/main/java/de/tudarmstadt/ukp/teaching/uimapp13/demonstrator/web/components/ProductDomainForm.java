package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.ProductDomain;

public class ProductDomainForm
    extends Form<Void>
{
    private static final long serialVersionUID = -3501393706508814294L;

    @SuppressWarnings("unused")
    private ProductDomain selectedProductDomain;

    public ProductDomainForm(final String id, final ProductDomain selectedProductDomain)
    {
        super(id);
        final ChoiceRenderer<ProductDomain> productDomainChoiceRenderer = new ChoiceRenderer<ProductDomain>(
                "name", "id");

        this.selectedProductDomain = selectedProductDomain;
        final PropertyModel<ProductDomain> selectedDomainModel = new PropertyModel<ProductDomain>(
                this, "selectedProductDomain");
        // final Select<ProductDomain> domainSelect = new
        // Select<ProductDomain>("productDomainChoice",
        // selectedDomainModel);
        // for (final ProductDomain domain : ProductDomain.getAllDomains()) {
        // domainSelect.add(new SelectOption<ProductDomain>("" + domain.getId()));
        // }
        final DropDownChoice<ProductDomain> domainSelect = new DropDownChoice<ProductDomain>(
                "productDomainChoice", selectedDomainModel, ProductDomain.getAllDomains(),
                productDomainChoiceRenderer);
        this.add(domainSelect);
    }
}
