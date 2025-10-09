function CheckDelete(){
	if(confirm('勤怠記録を削除しますか？')){
		return true;
	} else{
		return false;
	}
}

function CheckAlter(){
	if(confirm('変更しますか?')){
		return true;
	} else{
		return false;
	}
}

function toggleAll(masterCheckbox) {
    // グループ識別子を取得
    const groupId = masterCheckbox.getAttribute('data-group');

    // グループに属するすべてのチェックボックスを取得
    const checkboxes = document.querySelectorAll('.' + groupId.replace('group__', 'checkbox__'));

    // マスターの状態に応じてON/OFF
    checkboxes.forEach(cb => cb.checked = masterCheckbox.checked);
}