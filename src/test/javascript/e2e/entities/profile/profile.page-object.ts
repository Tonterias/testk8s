import { element, by, ElementFinder } from 'protractor';

export class ProfileComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-profile div table .btn-danger'));
    title = element.all(by.css('jhi-profile div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getText();
    }
}

export class ProfileUpdatePage {
    pageTitle = element(by.id('jhi-profile-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    creationDateInput = element(by.id('field_creationDate'));
    imageInput = element(by.id('file_image'));
    genderSelect = element(by.id('field_gender'));
    phoneInput = element(by.id('field_phone'));
    bioInput = element(by.id('field_bio'));
    facebookInput = element(by.id('field_facebook'));
    twitterInput = element(by.id('field_twitter'));
    linkedinInput = element(by.id('field_linkedin'));
    instagramInput = element(by.id('field_instagram'));
    optionalsnInput = element(by.id('field_optionalsn'));
    birthdateInput = element(by.id('field_birthdate'));
    sibblingsInput = element(by.id('field_sibblings'));
    petInput = element(by.id('field_pet'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setCreationDateInput(creationDate) {
        await this.creationDateInput.sendKeys(creationDate);
    }

    async getCreationDateInput() {
        return this.creationDateInput.getAttribute('value');
    }

    async setImageInput(image) {
        await this.imageInput.sendKeys(image);
    }

    async getImageInput() {
        return this.imageInput.getAttribute('value');
    }

    async setGenderSelect(gender) {
        await this.genderSelect.sendKeys(gender);
    }

    async getGenderSelect() {
        return this.genderSelect.element(by.css('option:checked')).getText();
    }

    async genderSelectLastOption() {
        await this.genderSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setPhoneInput(phone) {
        await this.phoneInput.sendKeys(phone);
    }

    async getPhoneInput() {
        return this.phoneInput.getAttribute('value');
    }

    async setBioInput(bio) {
        await this.bioInput.sendKeys(bio);
    }

    async getBioInput() {
        return this.bioInput.getAttribute('value');
    }

    async setFacebookInput(facebook) {
        await this.facebookInput.sendKeys(facebook);
    }

    async getFacebookInput() {
        return this.facebookInput.getAttribute('value');
    }

    async setTwitterInput(twitter) {
        await this.twitterInput.sendKeys(twitter);
    }

    async getTwitterInput() {
        return this.twitterInput.getAttribute('value');
    }

    async setLinkedinInput(linkedin) {
        await this.linkedinInput.sendKeys(linkedin);
    }

    async getLinkedinInput() {
        return this.linkedinInput.getAttribute('value');
    }

    async setInstagramInput(instagram) {
        await this.instagramInput.sendKeys(instagram);
    }

    async getInstagramInput() {
        return this.instagramInput.getAttribute('value');
    }

    async setOptionalsnInput(optionalsn) {
        await this.optionalsnInput.sendKeys(optionalsn);
    }

    async getOptionalsnInput() {
        return this.optionalsnInput.getAttribute('value');
    }

    async setBirthdateInput(birthdate) {
        await this.birthdateInput.sendKeys(birthdate);
    }

    async getBirthdateInput() {
        return this.birthdateInput.getAttribute('value');
    }

    async setSibblingsInput(sibblings) {
        await this.sibblingsInput.sendKeys(sibblings);
    }

    async getSibblingsInput() {
        return this.sibblingsInput.getAttribute('value');
    }

    getPetInput() {
        return this.petInput;
    }
    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class ProfileDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-profile-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-profile'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
