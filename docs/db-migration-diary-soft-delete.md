# Diary / DiaryMedia Soft Delete 마이그레이션 가이드

저장소에는 Flyway/Liquibase 가 도입되어 있지 않으므로 운영 DB 적용은 직접 수행한다.
아래 SQL 은 코드 변경(`Diary.deletedAt`, `DiaryMedia.deletedAt`, 유니크 제약 제거)에 대응한다.

## 1. 사전 확인

```sql
-- 현재 일기 스키마 / 제약 이름 확인
\d diary.diary_entry
\d storage.diary_media
```

## 2. 적용 (PostgreSQL)

```sql
BEGIN;

-- (1) diary.diary_entry 에 deleted_at 컬럼 추가
ALTER TABLE diary.diary_entry
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP NULL;

-- (2) 유니크 제약 제거 (정책: soft delete 후 같은 entry_date 재작성 허용)
ALTER TABLE diary.diary_entry
    DROP CONSTRAINT IF EXISTS uq_user_entry_date;

-- (3) storage.diary_media 에 deleted_at 컬럼 추가 (이미 존재하면 무시)
ALTER TABLE storage.diary_media
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP NULL;

COMMIT;
```

## 3. 검증

```sql
-- 컬럼 추가 확인
SELECT column_name, data_type, is_nullable
  FROM information_schema.columns
 WHERE (table_schema = 'diary'    AND table_name = 'diary_entry' AND column_name = 'deleted_at')
    OR (table_schema = 'storage'  AND table_name = 'diary_media'  AND column_name = 'deleted_at');

-- 유니크 제약 제거 확인 (결과가 0건이어야 한다)
SELECT conname FROM pg_constraint
 WHERE conname = 'uq_user_entry_date';
```

## 4. 주의

- 본 변경은 **코드 ↔ DB 의 deleted_at 컬럼 존재** 가 일치해야 동작한다.
  컬럼 추가 전 배포된 인스턴스가 일기를 soft delete 하면 `column "deleted_at" does not exist` 가 떨어진다.
  → 반드시 **DB 적용 → 배포** 순서를 지킨다.
- 유니크 제약 제거는 운영 데이터 영향이 없다(제약이 없으면 위반 row 가 없으므로).
  단, 이미 같은 `(user_info_id, entry_date)` 가 둘 이상 존재한다면 그대로 유지된다.
- 인덱스는 본 PR 에서 추가하지 않는다. `deleted_at IS NULL` 필터는 부분 유니크 인덱스가
  없는 만큼 풀 스캔 가능성이 있지만, 현 데이터 규모에서는 비용이 작다.
  향후 일기 수 증가 시 `(user_info_id, deleted_at)` 또는 부분 인덱스 도입을 검토한다.
- 일기 soft delete 시 첨부 미디어도 동일 트랜잭션으로 soft delete 된다.
  MinIO 객체 자체는 보존되므로 스토리지 비용은 누적된다(추후 라이프사이클 정책 별도).
