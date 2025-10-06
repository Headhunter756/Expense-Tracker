var message_box;

window.addEventListener("DOMContentLoaded",()=>{
    document.getElementById("user").textContent = sessionStorage.getItem("username");
    expenses();
    message_box = document.getElementById("message");
})

async function logout() {
    const response = await fetch("http://localhost:8080/auth/logout", {
    method: "POST",
    headers: {
        'Content-Type':"application/json",
    },
    body:JSON.stringify({
        'token':sessionStorage.getItem("token").substring(7)
    })
    });
    if 
    (response.ok) {
        const message = await response.text();
        localStorage.setItem("message", message);
        sessionStorage.removeItem("username");
        sessionStorage.removeItem("token");
        window.location.href = "login.html";
    }
    else{
        error("Some error occured while logging out");
    }
}

async function expenses() {
    document.getElementById("content").innerHTML = "";
    const response = await fetch("http://localhost:8080/expense/",{
        method: "GET",
        headers: {
            'Content-Type':"application/json",
            'Authorization':sessionStorage.getItem("token")
        }
    });
    if(response.ok){
        const data = await response.json();
        const arr = {};
        if (data.length!=0) {
            for(let a of data){
                var color = "";
                if (a.type=="DEBIT") {
                    color = "red";
                } else {
                    color = "green";
                }
                let newdate = formatdate(a.date);
                document.getElementById("content").innerHTML += 
                `<tr id="${a.id}">
                    <td>${a.id}</td>
                    <td>${a.category}</td>
                    <td>${a.name}</td>
                    <td>${a.amount}</td>
                    <td>${newdate}</td>
                    <td style="color:${color};">${a.type}</td>
                    <td>
                        <input type="button" onclick="remove(${a.id})" value="Delete">
                        <input type="button" onclick="openedit(${a.id})" value="Edit">
                    </td>
                </tr>`;
            }
        }
        else{
            document.getElementById("content").innerHTML = 
            `<tr>
                <th colspan = 7>No records Found</th>
            </tr>`;
        }
    }
    else{
        document.getElementById("content").innerHTML = `
        <tr>
            <th colspan = 7>No records Found</th>
        </tr>
        `;
        error("Some error occured while getting the expenses")
    }
}

function openedit(id) {
    const tr = document.getElementById(`${id}`);
    while (tr.firstChild) {
        tr.removeChild(tr.firstChild);
    }

    tr.innerHTML = `
    <td colspan="7">
        <form id="editform" class="edit-form">
            <div class="form-grid">
                <input type="number" name="id" id="id" readonly class="form-input">
                
                <select name="category" id="category" class="form-select">
                    <option value="Not to Mention">Not to Mention🤫</option>
                    <option value="Food">Food</option>
                    <option value="Travel">Travel</option>
                    <option value="Stationary">Stationary</option>
                    <option value="Money Transfer">Money Transfer</option>
                    <option value="Entertainment">Entertainment</option>
                    <option value="Shopping">Shopping</option>
                </select>
                
                <input type="text" name="name" id="name" placeholder="Expense Name" class="form-input">
                <input type="number" name="amount" id="amount" placeholder="Amount" class="form-input">
                <input type="datetime-local" name="date" id="date" class="form-input">
                
                <select name="type" id="type" required class="form-select">
                    <option value="DEBIT">DEBIT</option>
                    <option value="CREDIT">CREDIT</option>
                </select>
                
                <div class="form-actions">
                    <input type="button" value="✅ Edit" onclick="edit()" class="btn edit-btn">
                    <input type="button" value="❌ Cancel" onclick="expenses()" class="btn cancel-btn">
                </div>
            </div>
        </form>
    </td>
    `;

    insertdata(id);
}

async function insertdata(id) {
    const response = await fetch(`http://localhost:8080/expense/${id}`,{
        method: 'GET',
        headers:{
            'Content-Type':"application/json",
            'Authorization':sessionStorage.getItem("token")
        }
    });
    if(response.ok){
        const data = await response.json();
        document.getElementById("id").value = data.id;
        document.getElementById("category").value = data.category;
        document.getElementById("name").value = data.name;
        document.getElementById("amount").value = data.amount;
        document.getElementById("date").value = data.date;
        document.getElementById("type").value = data.type;
    }
    else{
        error("Couldn't insert data.");
    }   
}

async function edit() {
    const formobj = document.getElementById("editform");
    const formdata = new FormData(formobj);
    const data = Object.fromEntries(formdata);
    const response = await fetch(`http://localhost:8080/expense/`,{
        method:'PUT',
        headers:{
            'Content-Type':"application/json",
            'Authorization':sessionStorage.getItem("token")
        },
        body:JSON.stringify(data)
    });
    const responsedata = await response.json();
    if (!response.ok) {
        error(responsedata.message);
    }
    else{
        success(responsedata.message);
    }
    await expenses();
}

async function remove(id) {
    const response = await fetch(`http://localhost:8080/expense/${id}`,{
        method:"DELETE",
        headers:{
            'Content-Type': "application/json",
            'Authorization':sessionStorage.getItem("token")
        }
    })
    if (response.status==204) {
        success("Record successfully removed.")
        await expenses();
    }
    else{
        error("Couldn't delete data.");
    }
}

function openadd(){
    window.location.href = "/html/add.html";
}

function formatdate(olddate) {
    const date = new Date(olddate);
    var hours = date.getHours();
    const minutes = date.getMinutes();
    const time = (hours>=12) ? "PM":"AM";
    hours = (hours>12) ? (hours-12):hours; 
    const newdate = hours+":"+((minutes==0)? "00":minutes)+" "+time;
    return (date.toDateString()+" "+newdate);
}

function error(message){
    message_box.textContent = message;
    message_box.style.color = "red";
}

function success(message){
    message_box.textContent = message;
    message_box.style.color = "green";
}
function openProfile(){
    window.location.href="/html/profile.html";
}