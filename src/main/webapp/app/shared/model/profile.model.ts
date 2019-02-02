import { Moment } from 'moment';

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
    OTHER = 'OTHER'
}

export interface IProfile {
    id?: number;
    creationDate?: Moment;
    imageContentType?: string;
    image?: any;
    gender?: Gender;
    phone?: string;
    bio?: string;
    facebook?: string;
    twitter?: string;
    linkedin?: string;
    instagram?: string;
    optionalsn?: string;
    birthdate?: Moment;
    sibblings?: number;
    pet?: boolean;
}

export class Profile implements IProfile {
    constructor(
        public id?: number,
        public creationDate?: Moment,
        public imageContentType?: string,
        public image?: any,
        public gender?: Gender,
        public phone?: string,
        public bio?: string,
        public facebook?: string,
        public twitter?: string,
        public linkedin?: string,
        public instagram?: string,
        public optionalsn?: string,
        public birthdate?: Moment,
        public sibblings?: number,
        public pet?: boolean
    ) {
        this.pet = this.pet || false;
    }
}
