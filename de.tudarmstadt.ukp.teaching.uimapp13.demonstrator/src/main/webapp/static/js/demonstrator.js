/**
 * Author: Roland Kluge
 */

function updateDomainSpecificFormsVisibility() {
	var domain = $('#productDomainChoice')[0].value;
	$('.domainSpecificForm').css({
		display : 'none'
	});
	$('#' + domain + "-form").css({
		display : 'block'
	});
}

$(document).ready(function() {
	updateDomainSpecificFormsVisibility();
	$('#productDomainChoice').change(function() {
		updateDomainSpecificFormsVisibility();
	});
});