export default class {
    this;
    events;
    [event];
    return;

) {
    var;

!
    cb;
.
    of;
    this;
)
    events;
    [event];

    constructor() {
        this.events = {}
    }

    off(event) {
        delete this.events[event]
    }

    on(event, cb) {
        this.events[event] || (this.events[event] = []);
        this.events[event].push(cb)
    }

    emit(event,
         ...
             args;

.

    if(

    for()

    cb(
        ...
            args;

)
}
}
