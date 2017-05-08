var viewModel = {
	repositories : ko.observableArray()
};
ko.applyBindings(viewModel);
$.getJSON("/services/p2/repositories", function(data) {
	viewModel.repositories(data.repositories);
}).fail(function(jqXHR, textStatus, errorThrown) {
	alert('getJSON request failed! ' + textStatus + errorThrown);
})