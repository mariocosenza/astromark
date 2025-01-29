export class SelectedTicket {
    private static _ticketId: string | null = null;

    static get ticketId(): string | null {
        return this._ticketId;
    }

    static set ticketId(value: string) {
        this._ticketId = value;
    }


    private static _closed: boolean = true;

    static get closed(): boolean {
        return this._closed;
    }

    static set closed(value: boolean) {
        this._closed = value;
    }
}
