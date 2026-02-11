   /*==========Dashboard JavaScript==========*/




// dashboard refresh js
function refreshDashboard() {
            location.reload();
        }

// auto refresh
        setInterval(function () {
            location.reload();
        }, 60000);
		
		
		const CONTEXT_PATH = /*[[@{/}]]*/ '';
		
		
		// fetch the total Registred User and daywise register user
		fetch(CONTEXT_PATH + 'registerUserCount')
								            .then(res => {
								                if (!res.ok) throw new Error("Not logged in");
								                return res.json();
								            })
								            .then(response => {
								                document.getElementById("totalRegisterUserCount").innerText = response.userCount;
								                document.getElementById("totalDaywiseUserCount").innerText = response.todaysUserCount;
								                document.getElementById("todaysRegisterUser").innerText = response.todaysUserCount;
								            })
								            .catch(err => console.error(err));
											
											
											
											
		// fetch the total uploaded documents and daywise uploaded doc count
		fetch(CONTEXT_PATH + 'uploadedDocument')
										            .then(res => {
										                if (!res.ok) throw new Error("Not logged in");
										                return res.json();
										            })
										            .then(response => {
										                document.getElementById("totalUploadedDocCount").innerText = response.uploadedDocCount;
										                document.getElementById("totalDaywiseDocCount").innerText = response.daywiseCount;
										            })
										            .catch(err => console.error(err));
													
													
													
				
			// fetch the total acive shares 
			function loadOnlineUsers() {

			    fetch('onlineUsers')
			        .then(res => res.json())
			        .then(count => {
			            document.getElementById("activeUsers").innerText = count;
			        });
			}

			// Refresh every 10 seconds
			setInterval(loadOnlineUsers, 60000);

			document.addEventListener("DOMContentLoaded", loadOnlineUsers);

										