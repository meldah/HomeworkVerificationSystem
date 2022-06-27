function addRow(tableID) {

			var table = document.getElementById(tableID);

			var rowCount = table.rows.length;
			var row = table.insertRow(rowCount);

			var colCount = table.rows[2].cells.length;

			for(var i=0; i<colCount; i++) {

				var newcell	= row.insertCell(i);

				newcell.innerHTML = table.rows[2].cells[i].innerHTML;
				//alert(newcell.childNodes);
				switch(newcell.childNodes[1].type) {
					case "text":
							newcell.childNodes[1].value = "";
							break;
					case "number":
							newcell.childNodes[1].value = "0.0";
							break;
					case "checkbox":
							newcell.childNodes[1].checked = false;
							break;
					case "select-one":
							newcell.childNodes[1].selectedIndex = 0;
							break;
				}
			}
		}

		function deleteRow(tableID) {
			try {
			var table = document.getElementById(tableID);
			var rowCount = table.rows.length;

			for(var i=2; i < rowCount; i++) {
				var row = table.rows[i];
				var td = row.cells[0];
				var checkbx = td.childNodes[1];
				if(null != checkbx && true == checkbx.checked) {
					if(rowCount <= 3) {
						alert("Cannot delete all the rows.");
						break;
					}
					table.deleteRow(i);
					rowCount--;
					i--;
				}
			}
			}catch(e) {
				alert(e);
			}
		}