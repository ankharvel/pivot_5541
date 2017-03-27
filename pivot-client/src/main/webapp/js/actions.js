function reloadUploadFile(){
    document.getElementById('leftMenu:uploadFile').click();
}

function reloadReportPanel(){
    document.getElementById('leftMenu:reportPanel').click();
}

function reloadConfigurePanel(){
    document.getElementById('leftMenu:configurePanel').click();
}

function reloadExportPanel(){
    document.getElementById('leftMenu:exportPanel').click();
}

function clearDatabaseForm() {
    if(document.getElementById('dbForm:hostID') != null) {
        document.getElementById('dbForm:hostID').value = '';
        document.getElementById('dbForm:portID').value = '';
        document.getElementById('dbForm:databaseID').value = '';
        document.getElementById('dbForm:usernameID').value = '';
    }
}