# CollabLab - UML and Architecture Diagrams

Version 1.0 - design baseline for the `ft_transcendence` project.

## 1. Scope and assumptions

CollabLab is a private, multi-user collaboration platform. A registered user can create a workspace or join one through an invitation. Workspace resources are never accessible unless the authenticated user has an active membership.

The diagrams assume:

- Frontend: React or Next.js with TypeScript.
- Backend: NestJS with TypeScript.
- Database: PostgreSQL through Prisma ORM.
- Real-time transport: Socket.IO/WebSockets.
- Files: private object storage such as MinIO.
- Authentication: secure HTTP-only cookies or short-lived access tokens with refresh-token rotation.
- Workspace roles: `OWNER`, `ADMIN`, `MEMBER`, and `VIEWER`.
- A modular monolith is used initially; modules can later be extracted if necessary.

## 2. System context diagram

```mermaid
flowchart LR
    User["Registered user"]
    Guest["Unauthenticated visitor"]
    Mail["Email provider"]
    OAuth["OAuth provider"]
    Store["Object storage"]
    Collab["CollabLab platform"]

    Guest -->|Register or log in| Collab
    User <-->|HTTPS and WebSocket| Collab
    Collab -->|Invitation email| Mail
    Collab <-->|Optional sign-in| OAuth
    Collab <-->|Private files| Store
```

## 3. Use-case diagram

```mermaid
flowchart LR
    Visitor((Visitor))
    Member((Member))
    Admin((Admin))
    Owner((Owner))

    subgraph CollabLab
        Register([Register and log in])
        Profile([Manage profile])
        CreateWS([Create workspace])
        Accept([Accept invitation])
        ViewWS([Access workspace])
        Task([Manage tasks])
        Chat([Use team chat])
        Notes([Edit shared notes])
        Board([Use whiteboard])
        Calendar([Manage calendar])
        Files([Manage files])
        Analytics([View analytics])
        Invite([Invite and remove members])
        Roles([Manage member roles])
        Settings([Manage workspace settings])
        DeleteWS([Delete workspace])
    end

    Visitor --> Register
    Member --> Profile
    Member --> Accept
    Member --> ViewWS
    Member --> Task
    Member --> Chat
    Member --> Notes
    Member --> Board
    Member --> Calendar
    Member --> Files
    Member --> Analytics
    Admin --> Invite
    Admin --> Roles
    Admin --> Settings
    Owner --> DeleteWS
    Owner --> Roles
    CreateWS --> ViewWS
    Member --> CreateWS
```

Role specialization: an Owner has Admin and Member capabilities; an Admin has Member capabilities. A Viewer is a restricted Member with read-only access to workspace content.

## 4. Core domain class diagram

```mermaid
classDiagram
    class User {
      +UUID id
      +string email
      +string passwordHash
      +string displayName
      +string avatarUrl
      +UserStatus status
      +DateTime createdAt
      +updateProfile()
    }

    class Workspace {
      +UUID id
      +string name
      +string description
      +UUID ownerId
      +DateTime createdAt
      +updateSettings()
      +archive()
    }

    class WorkspaceMember {
      +UUID id
      +UUID userId
      +UUID workspaceId
      +WorkspaceRole role
      +MemberStatus status
      +DateTime joinedAt
      +changeRole()
      +remove()
    }

    class Invitation {
      +UUID id
      +UUID workspaceId
      +UUID invitedById
      +string email
      +string tokenHash
      +WorkspaceRole role
      +InvitationStatus status
      +DateTime expiresAt
      +accept()
      +revoke()
    }

    class ProjectTask {
      +UUID id
      +UUID workspaceId
      +UUID assigneeId
      +string title
      +string description
      +TaskStatus status
      +TaskPriority priority
      +DateTime dueAt
      +move()
      +assign()
    }

    class TaskComment {
      +UUID id
      +UUID taskId
      +UUID authorId
      +string body
      +DateTime createdAt
    }

    class Channel {
      +UUID id
      +UUID workspaceId
      +string name
      +ChannelType type
    }

    class Message {
      +UUID id
      +UUID channelId
      +UUID authorId
      +string body
      +DateTime createdAt
      +DateTime editedAt
    }

    class Note {
      +UUID id
      +UUID workspaceId
      +string title
      +JSON content
      +int version
      +DateTime updatedAt
    }

    class Whiteboard {
      +UUID id
      +UUID workspaceId
      +string name
      +JSON sceneData
      +int version
      +DateTime updatedAt
    }

    class CalendarEvent {
      +UUID id
      +UUID workspaceId
      +UUID creatorId
      +string title
      +DateTime startsAt
      +DateTime endsAt
      +string location
    }

    class StoredFile {
      +UUID id
      +UUID workspaceId
      +UUID uploadedById
      +string objectKey
      +string originalName
      +string mimeType
      +long sizeBytes
    }

    class Notification {
      +UUID id
      +UUID recipientId
      +NotificationType type
      +JSON payload
      +DateTime readAt
    }

    class ActivityEvent {
      +UUID id
      +UUID workspaceId
      +UUID actorId
      +string action
      +string entityType
      +UUID entityId
      +JSON metadata
      +DateTime createdAt
    }

    User "1" --> "0..*" Workspace : owns
    User "1" --> "0..*" WorkspaceMember : has
    Workspace "1" *-- "1..*" WorkspaceMember : contains
    Workspace "1" *-- "0..*" Invitation
    Workspace "1" *-- "0..*" ProjectTask
    ProjectTask "1" *-- "0..*" TaskComment
    Workspace "1" *-- "1..*" Channel
    Channel "1" *-- "0..*" Message
    Workspace "1" *-- "0..*" Note
    Workspace "1" *-- "0..*" Whiteboard
    Workspace "1" *-- "0..*" CalendarEvent
    Workspace "1" *-- "0..*" StoredFile
    User "1" --> "0..*" Notification : receives
    Workspace "1" *-- "0..*" ActivityEvent
```

## 5. Database entity-relationship diagram

```mermaid
erDiagram
    USER ||--o{ WORKSPACE : owns
    USER ||--o{ WORKSPACE_MEMBER : joins
    WORKSPACE ||--|{ WORKSPACE_MEMBER : contains
    WORKSPACE ||--o{ INVITATION : issues
    USER ||--o{ INVITATION : sends
    WORKSPACE ||--o{ PROJECT_TASK : contains
    USER o|--o{ PROJECT_TASK : assigned_to
    PROJECT_TASK ||--o{ TASK_COMMENT : receives
    USER ||--o{ TASK_COMMENT : writes
    WORKSPACE ||--o{ CHANNEL : contains
    CHANNEL ||--o{ MESSAGE : contains
    USER ||--o{ MESSAGE : writes
    WORKSPACE ||--o{ NOTE : contains
    WORKSPACE ||--o{ WHITEBOARD : contains
    WORKSPACE ||--o{ CALENDAR_EVENT : contains
    WORKSPACE ||--o{ STORED_FILE : contains
    USER ||--o{ STORED_FILE : uploads
    USER ||--o{ NOTIFICATION : receives
    WORKSPACE ||--o{ ACTIVITY_EVENT : records

    USER {
      uuid id PK
      varchar email UK
      varchar password_hash
      varchar display_name
      varchar avatar_url
      varchar status
      timestamptz created_at
    }
    WORKSPACE {
      uuid id PK
      uuid owner_id FK
      varchar name
      text description
      timestamptz created_at
    }
    WORKSPACE_MEMBER {
      uuid id PK
      uuid workspace_id FK
      uuid user_id FK
      varchar role
      varchar status
      timestamptz joined_at
    }
    INVITATION {
      uuid id PK
      uuid workspace_id FK
      uuid invited_by_id FK
      varchar email
      varchar token_hash UK
      varchar role
      varchar status
      timestamptz expires_at
    }
    PROJECT_TASK {
      uuid id PK
      uuid workspace_id FK
      uuid assignee_id FK
      varchar title
      varchar status
      varchar priority
      timestamptz due_at
    }
    TASK_COMMENT {
      uuid id PK
      uuid task_id FK
      uuid author_id FK
      text body
      timestamptz created_at
    }
    CHANNEL {
      uuid id PK
      uuid workspace_id FK
      varchar name
      varchar type
    }
    MESSAGE {
      uuid id PK
      uuid channel_id FK
      uuid author_id FK
      text body
      timestamptz created_at
    }
    NOTE {
      uuid id PK
      uuid workspace_id FK
      varchar title
      jsonb content
      int version
    }
    WHITEBOARD {
      uuid id PK
      uuid workspace_id FK
      varchar name
      jsonb scene_data
      int version
    }
    CALENDAR_EVENT {
      uuid id PK
      uuid workspace_id FK
      uuid creator_id FK
      varchar title
      timestamptz starts_at
      timestamptz ends_at
    }
    STORED_FILE {
      uuid id PK
      uuid workspace_id FK
      uuid uploaded_by_id FK
      varchar object_key UK
      varchar original_name
      varchar mime_type
      bigint size_bytes
    }
    NOTIFICATION {
      uuid id PK
      uuid recipient_id FK
      varchar type
      jsonb payload
      timestamptz read_at
    }
    ACTIVITY_EVENT {
      uuid id PK
      uuid workspace_id FK
      uuid actor_id FK
      varchar action
      varchar entity_type
      uuid entity_id
      jsonb metadata
      timestamptz created_at
    }
```

Required database constraints:

- Unique membership on `(workspace_id, user_id)`.
- Unique active invitation on `(workspace_id, email)` where appropriate.
- Every workspace owner must also have an `OWNER` membership.
- Cascading deletion should be used carefully; activity/audit records may require retention or anonymization.
- Index all foreign keys plus common filters such as task status, message creation time, and notification recipient/read state.

## 6. Sequence - registration and login

```mermaid
sequenceDiagram
    actor User
    participant UI as Web client
    participant API as Auth API
    participant DB as PostgreSQL

    User->>UI: Submit email and password
    UI->>API: POST /auth/register over HTTPS
    API->>API: Validate and normalize input
    API->>DB: Check unique email
    API->>API: Hash password with Argon2/bcrypt
    API->>DB: Create user
    DB-->>API: User record
    API-->>UI: Secure session cookie + profile
    UI-->>User: Open dashboard

    Note over UI,API: Login follows the same validation path and verifies the password hash
```

## 7. Sequence - workspace invitation and protected access

```mermaid
sequenceDiagram
    actor Admin
    actor Invitee
    participant UI as Web client
    participant API as Workspace API
    participant DB as PostgreSQL
    participant Mail as Email service

    Admin->>UI: Invite user by email
    UI->>API: POST /workspaces/:id/invitations
    API->>DB: Verify Admin membership and permission
    alt Not authorized
        API-->>UI: 403 Forbidden
    else Authorized
        API->>API: Generate random token and store hash
        API->>DB: Create pending invitation with expiry
        API->>Mail: Send invitation link with raw token
        API-->>UI: Invitation created
    end

    Invitee->>UI: Open invitation link
    UI->>API: POST /invitations/:token/accept
    API->>DB: Find token hash and lock invitation
    API->>API: Verify recipient, status and expiration
    API->>DB: Transaction: create membership + mark accepted
    API-->>UI: Workspace membership created

    Invitee->>UI: Open workspace
    UI->>API: GET /workspaces/:id
    API->>DB: Verify active membership
    alt Active member
        API-->>UI: Workspace data
    else No membership
        API-->>UI: 403 Forbidden
    end
```

## 8. Sequence - task update with real-time synchronization

```mermaid
sequenceDiagram
    actor MemberA as Member A
    participant ClientA as Client A
    participant API as Task API
    participant DB as PostgreSQL
    participant WS as WebSocket gateway
    participant ClientB as Client B
    actor MemberB as Member B

    MemberA->>ClientA: Move task to Done
    ClientA->>API: PATCH /tasks/:id {status: DONE, version}
    API->>DB: Verify workspace membership and edit permission
    API->>DB: Update task if version matches
    alt Version conflict
        API-->>ClientA: 409 Conflict + latest task
        ClientA-->>MemberA: Show conflict and refresh
    else Updated
        API->>DB: Insert activity and notifications
        API->>WS: Publish task.updated to workspace room
        API-->>ClientA: Updated task
        WS-->>ClientB: task.updated event
        ClientB-->>MemberB: Kanban updates immediately
    end
```

## 9. Sequence - real-time team chat

```mermaid
sequenceDiagram
    actor Sender
    participant A as Sender client
    participant WS as WebSocket gateway
    participant Auth as Authorization service
    participant DB as PostgreSQL
    participant B as Team clients

    A->>WS: message.send(channelId, body, clientId)
    WS->>Auth: Validate session and channel membership
    alt Unauthorized
        WS-->>A: message.error(Forbidden)
    else Authorized
        WS->>DB: Persist message using clientId for idempotency
        DB-->>WS: Stored message
        WS-->>A: message.ack(messageId)
        WS-->>B: message.created(message)
    end

    Note over A,B: Reconnection retrieves messages after the last received cursor
```

## 10. Sequence - collaborative whiteboard or notes

```mermaid
sequenceDiagram
    actor EditorA as Editor A
    participant A as Client A
    participant Sync as Collaboration gateway
    participant Auth as Authorization service
    participant DB as Snapshot store
    participant B as Client B
    actor EditorB as Editor B

    A->>Sync: Join document room
    Sync->>Auth: Verify workspace membership
    Sync-->>A: Current snapshot + version
    EditorA->>A: Edit content
    A->>Sync: Send operation/update
    Sync->>Sync: Validate, merge and order update
    Sync-->>B: Broadcast merged update
    B-->>EditorB: Render remote edit
    Sync->>DB: Periodically persist snapshot and version

    Note over A,B: Use CRDT/Yjs or a documented conflict-resolution strategy
```

## 11. Activity diagram - workspace creation and collaboration

```mermaid
flowchart TD
    Start([Start]) --> Login{Authenticated?}
    Login -- No --> Auth[Register or log in]
    Auth --> Dashboard
    Login -- Yes --> Dashboard[Open dashboard]
    Dashboard --> Choice{Choose action}
    Choice -->|Create| Form[Enter workspace information]
    Form --> Validate{Valid input?}
    Validate -- No --> Form
    Validate -- Yes --> Create[Create workspace and Owner membership]
    Choice -->|Open existing| Check{Active membership?}
    Check -- No --> Denied[Return 403 and dashboard]
    Check -- Yes --> Workspace[Open workspace]
    Create --> Workspace
    Workspace --> Collaborate[Tasks, chat, notes, board, files, calendar]
    Collaborate --> Record[Record activity and notify members]
    Record --> Workspace
```

## 12. State diagram - invitation lifecycle

```mermaid
stateDiagram-v2
    [*] --> Pending: Invitation created
    Pending --> Accepted: Valid recipient accepts
    Pending --> Declined: Recipient declines
    Pending --> Revoked: Admin revokes
    Pending --> Expired: Expiration reached
    Accepted --> [*]
    Declined --> [*]
    Revoked --> [*]
    Expired --> [*]
```

## 13. State diagram - task lifecycle

```mermaid
stateDiagram-v2
    [*] --> Todo: Task created
    Todo --> Doing: Work starts
    Doing --> Review: Submit for review
    Review --> Doing: Changes requested
    Review --> Done: Approved
    Todo --> Archived: Cancel
    Doing --> Archived: Cancel
    Done --> Archived: Archive
    Archived --> [*]
```

## 14. Component diagram

```mermaid
flowchart TB
    Web["React / Next.js client"]
    Proxy["Nginx - HTTPS reverse proxy"]

    subgraph Backend["NestJS modular monolith"]
        Auth["Authentication module"]
        Users["Users and friends"]
        Workspaces["Workspaces and permissions"]
        Tasks["Tasks and calendar"]
        Realtime["Chat and collaboration gateway"]
        Content["Notes, boards and files"]
        Notify["Notifications and activity"]
        Analytics["Analytics and export"]
    end

    DB[(PostgreSQL)]
    Redis[(Redis - optional)]
    Files[(MinIO object storage)]
    Email["Email provider"]

    Web -->|HTTPS / WSS| Proxy
    Proxy --> Backend
    Auth --> DB
    Users --> DB
    Workspaces --> DB
    Tasks --> DB
    Realtime --> DB
    Realtime --> Redis
    Content --> DB
    Content --> Files
    Notify --> DB
    Notify --> Email
    Analytics --> DB
```

## 15. Deployment diagram

```mermaid
flowchart TB
    Browser["User browser"]

    subgraph Host["Docker host"]
        Proxy["Nginx container\nTLS termination"]
        Frontend["Frontend container"]
        Backend["NestJS API container"]
        Worker["Background worker container"]
        Database[("PostgreSQL container")]
        Cache[("Redis container")]
        Storage[("MinIO container")]
    end

    Browser -->|HTTPS / WSS| Proxy
    Proxy --> Frontend
    Proxy --> Backend
    Backend --> Database
    Backend --> Cache
    Backend --> Storage
    Worker --> Database
    Worker --> Cache
    Worker --> Storage
```

Only the reverse proxy should expose public ports. PostgreSQL, Redis, MinIO and internal services should use a private Docker network.

## 16. Authorization decision flow

```mermaid
flowchart TD
    Request["HTTP request or WebSocket event"] --> Session{Valid session?}
    Session -- No --> Unauthorized["401 Unauthorized"]
    Session -- Yes --> Resource["Load resource and workspace ID"]
    Resource --> Member{Active workspace membership?}
    Member -- No --> Forbidden["403 Forbidden"]
    Member -- Yes --> Permission{Role permits action?}
    Permission -- No --> Forbidden
    Permission -- Yes --> Validate["Validate payload and business rules"]
    Validate --> Execute["Execute transaction"]
    Execute --> Audit["Record activity and notify"]
    Audit --> Success["Return result / broadcast event"]
```

Never accept a workspace ID from the client without independently checking the resource and membership in the backend.

## 17. Permission matrix

| Action | Owner | Admin | Member | Viewer |
|---|:---:|:---:|:---:|:---:|
| View workspace content | Yes | Yes | Yes | Yes |
| Create and update tasks/content | Yes | Yes | Yes | No |
| Delete own messages/comments | Yes | Yes | Yes | No |
| Invite members | Yes | Yes | No | No |
| Remove members | Yes | Yes* | No | No |
| Change roles | Yes | Limited* | No | No |
| Update workspace settings | Yes | Yes | No | No |
| Transfer ownership | Yes | No | No | No |
| Delete workspace | Yes | No | No | No |

`*` An Admin cannot remove, demote or replace the Owner and should not grant the Owner role.

## 18. Recommended API boundaries

| Module | Example endpoints/events |
|---|---|
| Authentication | `POST /auth/register`, `POST /auth/login`, `POST /auth/refresh`, `POST /auth/logout` |
| Workspaces | `GET/POST /workspaces`, `GET/PATCH/DELETE /workspaces/:id` |
| Membership | `GET /workspaces/:id/members`, invite, accept, revoke, role update, remove |
| Tasks | Task CRUD, assignment, status change, comments, `task.updated` |
| Chat | Channel CRUD, message history, `message.send`, `message.created`, typing/read events |
| Notes/boards | Snapshot retrieval, versioned updates, join/leave document events |
| Calendar | Event CRUD and upcoming-deadline queries |
| Files | Metadata, signed upload/download, validation, preview and delete |
| Notifications | List, mark read, real-time `notification.created` |
| Analytics | Date-filtered aggregates plus CSV/PDF export |

## 19. Security and concurrency rules

- Apply authentication, membership and role checks to every HTTP request and WebSocket event.
- Use transactions for accepting invitations, transferring ownership, deleting members and multi-record changes.
- Use optimistic concurrency (`version`) for tasks, notes and boards where overwrites are possible.
- Use idempotency/client IDs for messages and retryable mutations.
- Store only invitation-token hashes; make tokens single-use and time-limited.
- Validate inputs on both client and server, while treating server validation as authoritative.
- Rate-limit authentication, invitations, chat and file endpoints.
- Validate file type, size and content; never trust only the extension or browser MIME type.
- Produce structured audit/activity events for important actions.
- Keep secrets in ignored environment files and provide `.env.example` without real secrets.
- Use HTTPS/WSS for every browser-to-backend connection.

## 20. Suggested implementation order

1. Database schema, authentication and session handling.
2. Workspace, membership, invitation and authorization guards.
3. Tasks, comments and activity events.
4. WebSocket rooms, presence, chat and notifications.
5. Notes and whiteboard synchronization.
6. Calendar and file management.
7. Analytics, export, testing, hardening and documentation.

These diagrams should be updated whenever the team changes the domain model, permissions, technical stack or module selection.
