package com.test.k8s.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.test.k8s.domain.enumeration.Gender;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Profile entity. This class is used in ProfileResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /profiles?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfileCriteria implements Serializable {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private GenderFilter gender;

    private StringFilter phone;

    private StringFilter bio;

    private StringFilter facebook;

    private StringFilter twitter;

    private StringFilter linkedin;

    private StringFilter instagram;

    private StringFilter optionalsn;

    private InstantFilter birthdate;

    private IntegerFilter sibblings;

    private BooleanFilter pet;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getBio() {
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
    }

    public StringFilter getFacebook() {
        return facebook;
    }

    public void setFacebook(StringFilter facebook) {
        this.facebook = facebook;
    }

    public StringFilter getTwitter() {
        return twitter;
    }

    public void setTwitter(StringFilter twitter) {
        this.twitter = twitter;
    }

    public StringFilter getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(StringFilter linkedin) {
        this.linkedin = linkedin;
    }

    public StringFilter getInstagram() {
        return instagram;
    }

    public void setInstagram(StringFilter instagram) {
        this.instagram = instagram;
    }

    public StringFilter getOptionalsn() {
        return optionalsn;
    }

    public void setOptionalsn(StringFilter optionalsn) {
        this.optionalsn = optionalsn;
    }

    public InstantFilter getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(InstantFilter birthdate) {
        this.birthdate = birthdate;
    }

    public IntegerFilter getSibblings() {
        return sibblings;
    }

    public void setSibblings(IntegerFilter sibblings) {
        this.sibblings = sibblings;
    }

    public BooleanFilter getPet() {
        return pet;
    }

    public void setPet(BooleanFilter pet) {
        this.pet = pet;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCriteria that = (ProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(facebook, that.facebook) &&
            Objects.equals(twitter, that.twitter) &&
            Objects.equals(linkedin, that.linkedin) &&
            Objects.equals(instagram, that.instagram) &&
            Objects.equals(optionalsn, that.optionalsn) &&
            Objects.equals(birthdate, that.birthdate) &&
            Objects.equals(sibblings, that.sibblings) &&
            Objects.equals(pet, that.pet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        gender,
        phone,
        bio,
        facebook,
        twitter,
        linkedin,
        instagram,
        optionalsn,
        birthdate,
        sibblings,
        pet
        );
    }

    @Override
    public String toString() {
        return "ProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (bio != null ? "bio=" + bio + ", " : "") +
                (facebook != null ? "facebook=" + facebook + ", " : "") +
                (twitter != null ? "twitter=" + twitter + ", " : "") +
                (linkedin != null ? "linkedin=" + linkedin + ", " : "") +
                (instagram != null ? "instagram=" + instagram + ", " : "") +
                (optionalsn != null ? "optionalsn=" + optionalsn + ", " : "") +
                (birthdate != null ? "birthdate=" + birthdate + ", " : "") +
                (sibblings != null ? "sibblings=" + sibblings + ", " : "") +
                (pet != null ? "pet=" + pet + ", " : "") +
            "}";
    }

}
