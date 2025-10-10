export interface CreateEventDto {
    name: string,
    description: string,
    date: string,
    type: number,
    address: string,
    price: number,
    recurrent: boolean
}