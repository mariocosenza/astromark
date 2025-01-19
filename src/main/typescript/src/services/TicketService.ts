
export class SelectedTicket {
    static get ticketId(): string | null {
        return this._ticketId;
    }

    static set ticketId(value: string) {
        this._ticketId = value;
    }
    private static _ticketId: string | null = null;

}
