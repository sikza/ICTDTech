$(document)
		.ready(
				function() {
					$("#addcheckup").on('click', function() {

						$("#addcheckuptable").fadeIn();
					});
					$("#showcheckups").on("click", function() {
						loadCheckups();
					});

					$("#deliverystatus")
							.on(
									"change",
									function() {
										var bsucess = document
												.createElement("select");
										var bsucessValue1 = document
												.createElement("option");
										bsucessValue1.setAttribute("value",
												"success");
										bsucessValue1.appendChild(document
												.createTextNode("Success"));
										var bsucessValue2 = document
												.createElement("option");
										bsucessValue2.setAttribute("value",
												"failure");
										bsucessValue2.appendChild(document
												.createTextNode("Failure"));
										bsucess.appendChild(bsucessValue1);
										bsucess.appendChild(bsucessValue2);
										var span1= document.createElement("span");
										span1.appendChild(bsucess);

										var lizo = "  <tr class='childinfo'><td></td><td  style='background-color:wheat;'>"
												+ "Child Name :(s) <br/><input type='text' name='childNames'/><br/>"
												+ "Surname : <br/>  <input type='text' name='childSurname'/><br/>"
												+ "Gender : <br/> <input type='text' name='gender'/><br/>"
												+ "Weight : <br/> <input type='text' name='childWeight'/><br/>"
												+ "Race : <br/>  <input type='text' name='childRace'/>"
												+ " </td></tr>";

										if ($(this).closest("select").val() == "yes") {
											$(this).closest("tr").after(span1)
													.hide().fadeIn(2100);
										} else {
											$(".childinfo").fadeOut(1500);
										}

									});
				});