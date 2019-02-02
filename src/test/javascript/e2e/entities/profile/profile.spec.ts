/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProfileComponentsPage, ProfileDeleteDialog, ProfileUpdatePage } from './profile.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Profile e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let profileUpdatePage: ProfileUpdatePage;
    let profileComponentsPage: ProfileComponentsPage;
    let profileDeleteDialog: ProfileDeleteDialog;
    const fileNameToUpload = 'logo-jhipster.png';
    const fileToUpload = '../../../../../main/webapp/content/images/' + fileNameToUpload;
    const absolutePath = path.resolve(__dirname, fileToUpload);

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Profiles', async () => {
        await navBarPage.goToEntity('profile');
        profileComponentsPage = new ProfileComponentsPage();
        await browser.wait(ec.visibilityOf(profileComponentsPage.title), 5000);
        expect(await profileComponentsPage.getTitle()).to.eq('Profiles');
    });

    it('should load create Profile page', async () => {
        await profileComponentsPage.clickOnCreateButton();
        profileUpdatePage = new ProfileUpdatePage();
        expect(await profileUpdatePage.getPageTitle()).to.eq('Create or edit a Profile');
        await profileUpdatePage.cancel();
    });

    it('should create and save Profiles', async () => {
        const nbButtonsBeforeCreate = await profileComponentsPage.countDeleteButtons();

        await profileComponentsPage.clickOnCreateButton();
        await promise.all([
            profileUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            profileUpdatePage.setImageInput(absolutePath),
            profileUpdatePage.genderSelectLastOption(),
            profileUpdatePage.setPhoneInput('phone'),
            profileUpdatePage.setBioInput('bio'),
            profileUpdatePage.setFacebookInput('facebook'),
            profileUpdatePage.setTwitterInput('twitter'),
            profileUpdatePage.setLinkedinInput('linkedin'),
            profileUpdatePage.setInstagramInput('instagram'),
            profileUpdatePage.setOptionalsnInput('optionalsn'),
            profileUpdatePage.setBirthdateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            profileUpdatePage.setSibblingsInput('5')
        ]);
        expect(await profileUpdatePage.getCreationDateInput()).to.contain('2001-01-01T02:30');
        expect(await profileUpdatePage.getImageInput()).to.endsWith(fileNameToUpload);
        expect(await profileUpdatePage.getPhoneInput()).to.eq('phone');
        expect(await profileUpdatePage.getBioInput()).to.eq('bio');
        expect(await profileUpdatePage.getFacebookInput()).to.eq('facebook');
        expect(await profileUpdatePage.getTwitterInput()).to.eq('twitter');
        expect(await profileUpdatePage.getLinkedinInput()).to.eq('linkedin');
        expect(await profileUpdatePage.getInstagramInput()).to.eq('instagram');
        expect(await profileUpdatePage.getOptionalsnInput()).to.eq('optionalsn');
        expect(await profileUpdatePage.getBirthdateInput()).to.contain('2001-01-01T02:30');
        expect(await profileUpdatePage.getSibblingsInput()).to.eq('5');
        const selectedPet = profileUpdatePage.getPetInput();
        if (await selectedPet.isSelected()) {
            await profileUpdatePage.getPetInput().click();
            expect(await profileUpdatePage.getPetInput().isSelected()).to.be.false;
        } else {
            await profileUpdatePage.getPetInput().click();
            expect(await profileUpdatePage.getPetInput().isSelected()).to.be.true;
        }
        await profileUpdatePage.save();
        expect(await profileUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await profileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Profile', async () => {
        const nbButtonsBeforeDelete = await profileComponentsPage.countDeleteButtons();
        await profileComponentsPage.clickOnLastDeleteButton();

        profileDeleteDialog = new ProfileDeleteDialog();
        expect(await profileDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Profile?');
        await profileDeleteDialog.clickOnConfirmButton();

        expect(await profileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
