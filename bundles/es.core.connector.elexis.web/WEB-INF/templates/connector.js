var viewModel = {
	dbTypes : [ 'MySQL', 'PostgreSQL' ],
	selectedDbType : ko.observable('MySQL'),
	hostName : ko.observable(""),
	port : ko.observable(3306),
	username : ko.observable(""),
	password : ko.observable(""),
	dbname : ko.observable("")
};
ko.applyBindings(viewModel);

$.getJSON("/services/elexis/connector/connection", function(data) {
	viewModel.hostName(data.hostName);
	viewModel.port(data.port);
	viewModel.selectedDbType(data.rdbmsType);
	viewModel.username(data.username);
	viewModel.password(data.password);
	viewModel.dbname(data.databaseName);
}).fail(function(jqXHR, textStatus, errorThrown) {
	alert('getJSON request failed! ' + textStatus + errorThrown);
})