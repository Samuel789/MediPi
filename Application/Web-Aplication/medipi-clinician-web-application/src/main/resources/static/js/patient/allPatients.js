var refreshViewFrequency;
$(document).ready(function() {
	refreshViewFrequency = $("#refreshViewFrequency").val();
	//refreshViewFrequency = 1000000;
	showActiveMenu(NAVIGATION_LINK_MAP.PATIENT);
});

(function getPatientsDetails() {
	$.ajax( {
		url: '/clinician/patient/patientsJSON',
		success: function(patients) {
			hideErrorDiv();
			$("#patientTiles").empty();
			$.each(patients, function(counter, patient) {
				$("#patientTiles").prepend(getPatientTile(patient));
			});
		},
		complete: function() {
			//Set patient list refresh w.r.t refreshFrequency
			setTimeout(getPatientsDetails, refreshViewFrequency);
		},
		error: function(request, status, error) {
			showErrorDiv(request.responseText);
		}
	});
})();

function getPatientTile(patient) {
	var tileDiv =
		'<div class="col-sm-2" id="patient-' + patient.patientId + '">' +
			'<a href="/clinician/patient/' + patient.patientId +'">' +
				'<table class="tile-view">' +
					'<tr>' +
						'<th scope="col">' +
							patient.dateOfBirth.getStringDate_DDmmmYYYY_From_Timestamp() +
						'</th>' +
					'</tr>' +
					'<tr>' +
						(patient.patientStatus === 'INCOMPLETE_SCHEDULE' ? '<td class="limit-indicator incomplete-schedule"></td>' : (patient.patientStatus === 'WITHIN_THRESHOLD' ? '<td class="limit-indicator smiley"></td>' : '<td class="limit-indicator frowney"></td>')) +
					'</tr>' +
					'<tr>' +
						'<th scope="col">' +
							patient.firstName + ' ' +
							patient.lastName +
						'</th>' +
					'</tr>' +
				'</table>' +
			'</a>' +
		'</div>';
	return tileDiv;
}